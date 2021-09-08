package com.mrtwon.framex_premium.FragmentSearch

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.mrtwon.framex_premium.ContentResponse.ContentResponse
import com.mrtwon.framex_premium.MainActivity
import com.mrtwon.framex_premium.R
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.animators.FadeInAnimator
import kotlinx.android.synthetic.main.fragment_search.view.*
import kotlinx.android.synthetic.main.layout_error_load.*
import kotlinx.android.synthetic.main.layout_error_load.view.*
import kotlinx.android.synthetic.main.one_element_search.view.*
import kotlinx.coroutines.DelicateCoroutinesApi
import pl.droidsonroids.gif.GifImageView
import java.util.*
import kotlin.math.log

class FragmentSearch: Fragment(), View.OnClickListener {
    val vm: SearchViewModel by lazy { ViewModelProvider(this).get(SearchViewModel::class.java) }
    lateinit var rv: RecyclerView
    lateinit var text_input: TextInputEditText
    lateinit var welcome_search: ImageView
    lateinit var not_found: View
    lateinit var connect_error: View
    lateinit var load: GifImageView
    lateinit var loadInRv: GifImageView
    lateinit var layout_rv: LinearLayout
    val list = arrayListOf<ContentResponse>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        rv = view.recycler_view
        text_input = view.text_input
        welcome_search = view.welcome_image
        not_found = view.not_found
        connect_error = view.error_load
        load = view.gif_load
        layout_rv = view.findViewById(R.id.layout_rv)
        loadInRv = view.findViewById(R.id.gif_load_in_rv)
        connect_error.reload.setOnClickListener(this)
        rv.adapter = ScaleInAnimationAdapter(AlphaInAnimationAdapter(SearchAdapter(list))).apply {
            setDuration(200)
            setFirstOnly(false)
        }
        rv.itemAnimator = FadeInAnimator()
        rv.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        view.btn_search_description.setOnClickListener(this)
        return view
    }

    @DelicateCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observerTextInput()
        observerSearch()
        checkState()
        super.onViewCreated(view, savedInstanceState)
    }

    fun checkState(){
        if(list.isNotEmpty()){
            log("list isn't empty")
            clearVisibility()
            layout_rv.visibility = View.VISIBLE
        }else{
            log("list is empty")
        }
    }



    @DelicateCoroutinesApi
    fun observerTextInput(){
        text_input.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
                Log.i("self-search","string query: ${v.text}")
                list.clear()
                rv.adapter?.notifyDataSetChanged()
                vm.searchQuery.postValue(v.text.toString())
                return@setOnEditorActionListener true
            }else{
                Log.i("self-search","other key ... $actionId")
            }
            false
        }
    }

    fun observerSearch(){
        vm.connectErrorLiveData.observe(viewLifecycleOwner){
            if(it) clearVisibility()
            connect_error.visibility = if(it) View.VISIBLE else View.GONE
        }
        vm.notFoundLiveData.observe(viewLifecycleOwner){
            if(it) clearVisibility()
            not_found.visibility = if(it) View.VISIBLE else View.GONE
        }
        vm.loadLiveData.observe(viewLifecycleOwner){
            if (list.isNotEmpty()) {
                loadInRv.visibility = if (it) View.VISIBLE else View.GONE
            } else {
                if (it) clearVisibility()
                load.visibility = if (it) View.VISIBLE else View.GONE
            }
        }
        vm.searchContent.observe(viewLifecycleOwner){
            if(it == null) return@observe
            if (it.isEmpty() && list.isEmpty()) vm.notFoundLiveData.postValue(true)
            else {
                vm.notFoundLiveData.postValue(false)
                clearVisibility()
                list.addAll(it)
                rv.adapter?.notifyDataSetChanged()
                layout_rv.visibility = View.VISIBLE
            }
            vm.searchContent.postValue(null)
        }
    }
    fun clearVisibility(){
        not_found.visibility = View.GONE
        connect_error.visibility = View.GONE
        load.visibility = View.GONE
        welcome_search.visibility = View.GONE
        layout_rv.visibility = View.GONE
    }
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tv_title = itemView.title
        val char_category = itemView.char_category
        fun bind(content: ContentResponse){
            var title = content.ru_title
            if(content.year != null){
                title += " (${content.year})"
            }
            tv_title.text = title
            char_category.text = when(content.contentType){
                "movie" -> "Ф"
                else -> "С"
            }
            itemView.linear_layout.setOnClickListener{
                Log.i("self-search","click")
                val bundle = Bundle().apply {
                    putInt("id", content.id)
                }
            when(content.contentType){
                    "tv_series" -> {
                        (requireActivity() as MainActivity).navController.navigate(R.id.action_fragmentSearch_to_fragmentAboutSerial, bundle)
                    }
                    "movie" -> {
                        (requireActivity() as MainActivity).navController.navigate(R.id.action_fragmentSearch_to_fragmentAboutMovie, bundle)
                    }
                }
            }
        }
    }
    inner class SearchAdapter(val contentList: List<ContentResponse>): RecyclerView.Adapter<ViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = layoutInflater.inflate(R.layout.one_element_search, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            if(position == contentList.size-1){
                vm.nextPageTitle()
            }
            log("position ${position}")
            holder.bind(contentList[position])
        }

        override fun getItemCount(): Int {
            return contentList.size
        }

    }

    override fun onDestroyView() {
        log("onDestroyView()")
        super.onDestroyView()
    }
    override fun onStop() {
        log("onStop()")
        super.onStop()
    }

    override fun onPause() {
        log("onPause()")
        super.onPause()
    }

    override fun onResume() {
        log("onResume()")
        super.onResume()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_search_description -> {
                (requireActivity() as MainActivity).navController.navigate(R.id.action_fragmentSearch_to_fragmentSearchDescription)
            }
            R.id.reload -> {
                vm.searchQuery.postValue(vm.searchQuery.value)
            }
        }
    }
    fun log(s: String){
        Log.i("self-search",s)
    }
    data class StatusFilter(var checkMovie: Boolean = false, var checkSerial: Boolean = false)

}