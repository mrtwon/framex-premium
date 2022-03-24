package com.mrtwon.framex_premium.screen.fragmentTop.topCurrentYear

import android.app.Activity
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
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mrtwon.framex_premium.Helper.HelperFunction
import com.mrtwon.framex_premium.R
import com.mrtwon.framex_premium.app.appComponent
import com.mrtwon.framex_premium.domain.entity.Content
import com.mrtwon.framex_premium.domain.entity.ContentItemPage
import com.mrtwon.framex_premium.domain.entity.enum.ContentEnum
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.screen.fragmentTop.MainTopVM
import com.mrtwon.framex_premium.screen.fragmentTop.TopOpenCallback
import com.mrtwon.framex_premium.screen.mainActivity.MainActivityCallback
import com.mrtwon.testfirebase.paging.adapter.diff.FirestoreDiff
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_error_load.view.*
import pl.droidsonroids.gif.GifImageView
import javax.inject.Inject

class FragmentTopCurrentYear: Fragment(), TopOpenCallback {
    @Inject
    lateinit var viewModelFactory: TopCurrentYearVM.Factory
    @Inject
    lateinit var picasso: Picasso
    private var adapter: AdapterCurrentYear? = null
    private lateinit var rv: RecyclerView
    private lateinit var notFound: LinearLayout
    private lateinit var error: LinearLayout
    private lateinit var load: GifImageView
    private lateinit var currentContent: ContentEnum
    private var pagedList: PagedList<ContentItemPage>? = null
    private var mRecyclerViewState = Bundle()
    private val vm: TopCurrentYearVM by lazy {
        ViewModelProvider(this, viewModelFactory).get(TopCurrentYearVM::class.java)
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
        Log.i("self-top-content-view","onCreate()")
        arguments?.getString("contentEnum")?.let {
            Log.i("self-main", "input args string = $it")
            currentContent = ContentEnum.valueOf(it)
            Log.i("self-main","contentEnum = ${currentContent.type}")
        }
        vm.setPrimaryFilter(
            TopCurrentYearVM.PrimaryFilter(
                content = currentContent,
                year = HelperFunction.getYear()
            )
        )
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("self-top-content-view","onCreateView()")
        val view = layoutInflater.inflate(R.layout.recyclerview_top_element, container, false)
        rv = view.findViewById(R.id.recycler_view)
        notFound = view.findViewById(R.id.not_found)
        error = view.findViewById(R.id.error_load)
        load = view.findViewById(R.id.gif_load)
        error.reload.setOnClickListener{ vm.retry() }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i("self-top-content-view","onViewCreated()")
        rv.visibility = View.VISIBLE
        if(adapter == null){
            Log.i("self-top-content-view","adapter is null")
            adapter = AdapterCurrentYear(FirestoreDiff(), this, picasso)
            observerPagedList()
            observerFailure()
            observerLoad()
            observerNotFound()
            observerSuccessful()
            observerChangeFilter()
        }else{
            Log.i("self-top-content-view","adapter isn't null")
        }
        rv.adapter = adapter
        rv.layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        super.onViewCreated(view, savedInstanceState)
    }

    private fun observerChangeFilter(){
        mainTopVm.filterCurrentYearLD.observe(viewLifecycleOwner){ changedFilter ->
            vm.changeFilter(changedFilter)
        }
    }
    private fun observerPagedList(){
        vm.pagedListLiveData.observe(viewLifecycleOwner){
            Log.i("self-top-content-view","pagedList is null - ${it == null}")
            if(it == null) return@observe
            adapter?.submitList(it)
            pagedList = it
        }
    }

    private fun observerFailure(){
        vm.errorLiveData.observe(viewLifecycleOwner){
            Log.i("self-debug", "onFailure")
                cleanVisibility()
                error.visibility = View.VISIBLE
        }
    }

    private fun observerLoad(){
        vm.loadLiveData.observe(viewLifecycleOwner){
            Log.i("self-debug", "onFailure - state $it")
            if(it){
                cleanVisibility()
                load.visibility = View.VISIBLE
            }
        }
    }

    private fun observerNotFound(){
        vm.notFoundLiveData.observe(viewLifecycleOwner){
            Log.i("self-debug", "onNotFound - state $it")
            if(it){
                cleanVisibility()
                notFound.visibility = View.VISIBLE
            }
        }
    }

    private fun observerSuccessful(){
        vm.successfulLiveData.observe(viewLifecycleOwner){
            Log.i("self-debug", "onSuccessful $it")
            if(it){
                cleanVisibility()
                rv.visibility = View.VISIBLE
            }
        }
    }


    private fun cleanVisibility(){
        Log.i("self-debug", "cleanVisibility()")
        notFound.visibility = View.GONE
        error.visibility = View.GONE
        load.visibility = View.GONE
        rv.visibility = View.GONE
    }

    companion object{
        fun instance(contentEnum: ContentEnum): FragmentTopCurrentYear{
            return FragmentTopCurrentYear().apply {
                arguments = Bundle().apply {
                    putString("contentEnum", contentEnum.toString())
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
    /*override fun onSaveInstanceState(outState: Bundle) {
        Log.i("self-top-content-view","onSaveInstanceState()")
        rv.layoutManager?.onSaveInstanceState()?.let {
            outState.putParcelable("layout_manager",it)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.i("self-top-content-view", "onActivityCreated()")
        savedInstanceState?.getParcelable<Parcelable>("layout_manager")?.let {
            rv.layoutManager?.onRestoreInstanceState(it)
        }
        super.onActivityCreated(savedInstanceState)
    }*/
    override fun onPause() {
        mRecyclerViewState.putParcelable("recyclerView", rv.layoutManager?.onSaveInstanceState())
        super.onPause()
    }

    override fun onResume() {
        rv.layoutManager?.onRestoreInstanceState(mRecyclerViewState.getParcelable("recyclerView"))
        super.onResume()
    }
    /*override fun onSaveInstanceState(outState: Bundle) {
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
    }*/
}