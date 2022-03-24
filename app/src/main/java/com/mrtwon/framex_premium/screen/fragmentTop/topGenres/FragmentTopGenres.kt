package com.mrtwon.framex_premium.screen.fragmentTop.topGenres

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mrtwon.framex_premium.R
import com.mrtwon.framex_premium.app.appComponent
import com.mrtwon.framex_premium.domain.entity.Content
import com.mrtwon.framex_premium.domain.entity.enum.ContentEnum
import com.mrtwon.framex_premium.domain.entity.enum.GenresEnum
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.screen.fragmentTop.MainTopVM
import com.mrtwon.framex_premium.screen.fragmentTop.TopOpenCallback
import com.mrtwon.framex_premium.screen.mainActivity.MainActivityCallback
import com.mrtwon.testfirebase.paging.adapter.diff.FirestoreDiff
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_error_load.view.*
import pl.droidsonroids.gif.GifImageView
import javax.inject.Inject

class FragmentTopGenres: Fragment(), TopOpenCallback {
    @Inject
    lateinit var viewModelFactory: TopGenresVM.Factory
    @Inject
    lateinit var picasso: Picasso
    private var adapter: AdapterTopGenres? = null
    private lateinit var rv: RecyclerView
    private lateinit var notFound: LinearLayout
    private lateinit var error: LinearLayout
    private lateinit var load: GifImageView
    private lateinit var currentContent: ContentEnum
    private lateinit var currentGenres: GenresEnum
    private val vm: TopGenresVM by lazy {
        ViewModelProvider(this, viewModelFactory).get(TopGenresVM::class.java)
    }
    private val mainTopVm: MainTopVM by lazy {
        ViewModelProvider(requireParentFragment()).get(MainTopVM::class.java)
    }

    override fun onAttach(context: Context) {
        context
            .appComponent
            .createTopComponent()
            .inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        arguments?.getString("contentEnum")?.let {
            currentContent = ContentEnum.valueOf(it)
        }
        arguments?.getString("genresEnum")?.let {
            currentGenres = GenresEnum.valueOf(it)
        }
        vm.setPrimaryFilter(
            TopGenresVM.PrimaryFilter(
                genres = currentGenres,
                content = currentContent
            )
        )
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = layoutInflater.inflate(R.layout.recyclerview_top_element, container, false)
        rv = view.findViewById(R.id.recycler_view)
        notFound = view.findViewById(R.id.not_found)
        error = view.findViewById(R.id.error_load)
        load = view.findViewById(R.id.gif_load)
        error.reload.setOnClickListener{ vm.retry() }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rv.visibility = View.VISIBLE
        if(adapter == null){
            adapter = AdapterTopGenres(diff = FirestoreDiff(), callback = this, picasso = picasso)
            observerPagedList()
            observerFailure()
            observerFirstLoad()
            observerNotFound()
            observerSuccessful()
            observerChangeFilter()
        }
        rv.adapter = adapter
        rv.layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        super.onViewCreated(view, savedInstanceState)
    }

    private fun observerChangeFilter(){
        mainTopVm.filterGenresLD.observe(viewLifecycleOwner){ newFilter ->
            vm.changeFilter(newFilter)
        }
    }
    private fun observerPagedList(){
        vm.pagedListLiveData.observe(viewLifecycleOwner){
            adapter?.submitList(it)
        }
    }
    private fun observerFailure(){
        vm.errorLiveData.observe(viewLifecycleOwner){
            Log.i("self-top-genres","onFailure()")
            cleanVisibility()
            error.visibility = View.VISIBLE
        }
    }
    private fun observerFirstLoad(){
        vm.firstLoadLiveData.observe(viewLifecycleOwner){
            if(it){
                Log.i("self-top-genres","onLoad()")
                cleanVisibility()
                load.visibility = View.VISIBLE
            }
        }
    }
    private fun observerNotFound(){
        vm.notFoundLiveData.observe(viewLifecycleOwner){
            Log.i("self-top-genres","onNotFound() $it")
            if(it){
                cleanVisibility()
                notFound.visibility = View.VISIBLE
            }
        }
    }
    private fun observerSuccessful(){
        vm.successfulLiveData.observe(viewLifecycleOwner){
            if(it){
                Log.i("self-top-genres","onSuccessful() $it")
                cleanVisibility()
                rv.visibility = View.VISIBLE
            }
        }
    }
    private fun cleanVisibility(){
        notFound.visibility = View.GONE
        error.visibility = View.GONE
        load.visibility = View.GONE
        rv.visibility = View.GONE
    }

    companion object{
        fun instance(contentEnum: ContentEnum, genresEnum: GenresEnum): FragmentTopGenres {
            return FragmentTopGenres().apply {
                arguments = Bundle().apply {
                    putString("contentEnum", contentEnum.toString())
                    putString("genresEnum", genresEnum.toString())
                }
            }
        }
    }

    override fun onOpen(content: Content) {
        val bundle = Bundle().apply {
            putInt("id", content.id)
        }
        when(content.contentType){
            ContentEnum.Movie -> {
                (requireActivity() as MainActivityCallback)
                    .getNavController()
                    .navigate(R.id.fragmentAboutMovie, bundle)
            }
            ContentEnum.Serial -> {
                (requireActivity() as MainActivityCallback)
                    .getNavController()
                    .navigate(R.id.fragmentAboutSerial, bundle)
            }
            ContentEnum.Undefined -> {}
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        rv.layoutManager?.onSaveInstanceState()?.let {
            outState.putParcelable("layout_manager",it)
        }
        super.onSaveInstanceState(outState)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        savedInstanceState?.getParcelable<Parcelable>("layout_manager")?.let {
            rv.layoutManager?.onRestoreInstanceState(it)
        }
        super.onActivityCreated(savedInstanceState)
    }
}