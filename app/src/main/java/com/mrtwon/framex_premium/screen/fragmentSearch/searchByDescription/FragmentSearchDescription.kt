package com.mrtwon.framex_premium.screen.fragmentSearch.searchByDescription
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputEditText
import com.mrtwon.framex_premium.R
import com.mrtwon.framex_premium.app.appComponent
import com.mrtwon.framex_premium.domain.entity.Content
import com.mrtwon.framex_premium.domain.entity.enum.ContentEnum
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.screen.fragmentSearch.SearchCallback
import com.mrtwon.framex_premium.screen.mainActivity.MainActivityCallback
import com.mrtwon.testfirebase.paging.adapter.diff.FirestoreDiff
import kotlinx.android.synthetic.main.fragment_search.view.*
import kotlinx.android.synthetic.main.fragment_search.view.recycler_view
import kotlinx.android.synthetic.main.fragment_search.view.text_input
import kotlinx.android.synthetic.main.fragment_search.view.welcome_image
import kotlinx.android.synthetic.main.fragment_search_description.view.*
import kotlinx.android.synthetic.main.layout_error_load.view.*
import pl.droidsonroids.gif.GifImageView
import javax.inject.Inject


class FragmentSearchDescription: Fragment(), SearchCallback, View.OnClickListener {
    @Inject
    lateinit var viewModelFactory: SearchByDescriptionVM.Factory
    private val vm: SearchByDescriptionVM by lazy { ViewModelProvider(this, viewModelFactory).get(SearchByDescriptionVM::class.java) }
    private lateinit var rv: RecyclerView
    private lateinit var textInput: TextInputEditText
    private lateinit var welcomeSearch: ImageView
    private lateinit var notFound: View
    private lateinit var connectError: View
    private lateinit var load: GifImageView
    private lateinit var loadInRv: GifImageView
    private lateinit var layoutRv: LinearLayout
    private lateinit var hideHelp: ImageView
    private lateinit var helpLayout: LinearLayout

    private var adapter: AdapterSearchByDescription? = null

    override fun onAttach(context: Context) {
        context
            .appComponent
            .createSearchComponent()
            .inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_description, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rv = view.recycler_view
        textInput = view.text_input
        notFound = view.findViewById(R.id.not_found)
        connectError = view.findViewById(R.id.error_load)
        loadInRv = view.findViewById(R.id.gif_load_in_rv)
        load = view.findViewById(R.id.gif_load)
        layoutRv = view.findViewById(R.id.layout_rv)
        hideHelp = view.findViewById(R.id.hide_help)
        helpLayout = view.findViewById(R.id.help_layout)
        welcomeSearch = view.welcome_image
        if(adapter == null){
            adapter = AdapterSearchByDescription(FirestoreDiff(), this)
            connectError.reload.setOnClickListener{ vm.retry() }
            view.chip_one.setOnClickListener(this)
            view.chip_two.setOnClickListener(this)
            view.chip_three.setOnClickListener(this)
            view.chip_four.setOnClickListener(this)
            observerError()
            observerFirstLoad()
            observerLoad()
            observerNotFound()
            observerPagedList()
            observerTextInput()
            observerSuccessful()
        }
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        super.onViewCreated(view, savedInstanceState)
    }

    private fun observerPagedList(){
        vm.pagedListLD.observe(viewLifecycleOwner){ pagedList ->
            adapter?.submitList(pagedList)
        }
    }
    private fun observerSuccessful(){
        vm.successfulLD.observe(viewLifecycleOwner){
            Log.i("self-search-desc","onSuccessful() $it")
            if(it){
                clearVisibility()
                rv.visibility = View.VISIBLE
                layoutRv.visibility = View.VISIBLE
            }
        }
    }

    private fun observerNotFound(){
        vm.notFoundLD.observe(viewLifecycleOwner){ state ->
            Log.i("self-search-desc", "notFound $state")
            if(state){
                clearVisibility()
                notFound.visibility = View.VISIBLE
            }
        }
    }
    private fun observerError(){
        vm.errorLD.observe(viewLifecycleOwner){ state ->
              clearVisibility()
              connectError.visibility = View.VISIBLE
        }
    }
    private fun observerFirstLoad(){
        vm.firstLoadLD.observe(viewLifecycleOwner) { state ->
            Log.i("self-search-desc","onFirstLoad()")
            if (state) {
                clearVisibility()
                load.visibility = View.VISIBLE
            }
        }
    }
    private fun observerLoad(){
        vm.loadLD.observe(viewLifecycleOwner) { state ->
            if (state) {
                loadInRv.visibility = View.VISIBLE
            }else{
                loadInRv.visibility = View.GONE
            }
        }
    }
    private fun observerTextInput(){
        textInput.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
                val filter = when(vm.filterLD.value){
                    null -> SearchByDescriptionVM.Filter(
                        descriptionList = arrayListOf(v.text.toString())
                    )
                    else -> vm.filterLD.value!!.apply {
                        descriptionList = arrayListOf(v.text.toString())
                    }
                }
                vm.setFilter(filter = filter)
                return@setOnEditorActionListener true
            }
            false
        }
    }

    private fun clearVisibility(){
        notFound.visibility = View.GONE
        connectError.visibility = View.GONE
        load.visibility = View.GONE
        welcomeSearch.visibility = View.GONE
        layoutRv.visibility = View.GONE
    }

    override fun onOpen(content: Content) {
        val bundle = Bundle().apply {
            putInt("id", content.id)
        }
        when(content.contentType){
            ContentEnum.Serial -> {
                (requireActivity() as MainActivityCallback)
                    .getNavController()
                    .navigate(R.id.action_fragmentSearchDescription_to_fragmentAboutSerial, bundle)
            }
            ContentEnum.Movie -> {
                (requireActivity() as MainActivityCallback)
                    .getNavController()
                    .navigate(R.id.action_fragmentSearchDescription_to_fragmentAboutMovie, bundle)
            }
            ContentEnum.Undefined -> {}
        }
    }

    override fun onClick(v: View?) {
        val text = (v as Chip).text.toString()
        val filter = when(vm.filterLD.value){
            null -> SearchByDescriptionVM.Filter(
                descriptionList = arrayListOf(text)
            )
            else -> vm.filterLD.value!!.apply {
                descriptionList = arrayListOf(text)
            }
        }
        vm.setFilter(filter)
    }
}