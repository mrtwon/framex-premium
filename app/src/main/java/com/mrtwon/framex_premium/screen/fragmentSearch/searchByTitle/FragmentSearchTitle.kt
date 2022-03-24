package com.mrtwon.framex_premium.screen.fragmentSearch.searchByTitle

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
import com.google.android.material.button.MaterialButton
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
import kotlinx.android.synthetic.main.layout_error_load.view.*
import pl.droidsonroids.gif.GifImageView
import javax.inject.Inject

class FragmentSearchTitle : Fragment(), SearchCallback, View.OnClickListener {
    @Inject
    lateinit var viewModelFactory: SearchByTitleVM.Factory

    private val vm: SearchByTitleVM by lazy{
        ViewModelProvider(this, viewModelFactory).get(SearchByTitleVM::class.java)
    }

    private lateinit var rv: RecyclerView
    private lateinit var textInput: TextInputEditText
    private lateinit var welcomeSearch: ImageView
    private lateinit var notFound: View
    private lateinit var connectError: View
    private lateinit var load: GifImageView
    private lateinit var loadInRv: GifImageView
    private lateinit var layoutRv: LinearLayout

    private var adapter: AdapterSearchByTitle? = null

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
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rv = view.recycler_view
        textInput = view.text_input
        welcomeSearch = view.welcome_image
        notFound = view.not_found
        connectError = view.error_load
        load = view.gif_load
        layoutRv = view.findViewById(R.id.layout_rv)
        loadInRv = view.findViewById(R.id.gif_load_in_rv)
        if(adapter == null){
            adapter = AdapterSearchByTitle(FirestoreDiff(), this)
        }
        connectError.reload.setOnClickListener{ vm.retry() }
        view.findViewById<MaterialButton>(R.id.btn_search_description).setOnClickListener(this)
        observerTextInput()
        observerError()
        observerFirstLoad()
        observerLoad()
        observerNotFound()
        observerPagedList()
        observerSuccessful()
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        super.onViewCreated(view, savedInstanceState)
    }

    private fun observerPagedList(){
        vm.pagedListLD.observe(viewLifecycleOwner){ pagedList ->
            Log.i("self-test-search","submitListAdapter")
            adapter?.submitList(pagedList)
        }
    }

    private fun observerSuccessful(){
        vm.successfulLD.observe(viewLifecycleOwner){ state ->
            if(state){
                Log.i("self-test-search","onSuccessful()")
                clearVisibility()
                layoutRv.visibility = View.VISIBLE
                rv.visibility = View.VISIBLE
            }
        }
    }
    private fun observerNotFound(){
        vm.notFoundLD.observe(viewLifecycleOwner){ state ->
            if(state){
                Log.i("self-test-search","onNotFound()")
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
            Log.i("self-test-search","onFirstLoad $state")
            if (state) {
                clearVisibility()
                load.visibility = View.VISIBLE
            }
        }
    }
    private fun observerLoad(){
        vm.loadLD.observe(viewLifecycleOwner) { state ->
            Log.i("self-test-search","onLoad $state")
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
                if(vm.filterLD.value == null){
                    vm.setFilter(SearchByTitleVM.Filter(title = v.text.toString()))
                }else{
                    vm.filterLD.value?.let { vm.setFilter(it.apply{title = v.text.toString()}) }
                }
                return@setOnEditorActionListener true
            }else{
                Log.i("self-search","other key ... $actionId")
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
        val bundle = Bundle().apply { putInt("id", content.id) }
        when(content.contentType){
            ContentEnum.Serial -> {
                (requireActivity() as MainActivityCallback)
                    .getNavController()
                    .navigate(
                        R.id.action_fragmentSearch_to_fragmentAboutSerial, bundle
                    )
            }
            ContentEnum.Movie -> {
                (requireActivity() as MainActivityCallback)
                    .getNavController()
                    .navigate(
                        R.id.action_fragmentSearch_to_fragmentAboutMovie, bundle
                    )
            }
            ContentEnum.Undefined -> {}
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_search_description -> {
                (requireActivity() as MainActivityCallback)
                    .getNavController()
                    .navigate(R.id.fragmentSearchDescription)
            }
        }
    }
}