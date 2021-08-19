package com.mrtwon.framex_premium.FragmentSearch

import android.os.Bundle
import android.text.Html
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
import com.mrtwon.framex_premium.ContentResponse.ContentResponse
import com.mrtwon.framex_premium.MainActivity
import com.mrtwon.framex_premium.R
import kotlinx.android.synthetic.main.fragment_about_movie.view.*
import kotlinx.android.synthetic.main.fragment_search.view.recycler_view
import kotlinx.android.synthetic.main.fragment_search.view.text_input
import kotlinx.android.synthetic.main.fragment_search.view.welcome_search
import kotlinx.android.synthetic.main.fragment_search_description.view.*
import kotlinx.android.synthetic.main.layout_error_load.view.*
import kotlinx.android.synthetic.main.one_element_search.view.*
import kotlinx.android.synthetic.main.one_element_search.view.title
import kotlinx.coroutines.DelicateCoroutinesApi
import pl.droidsonroids.gif.GifImageView
import java.util.*

class FragmentSearchDescription: Fragment(), View.OnClickListener {
    val vm: SearchViewModel by lazy { ViewModelProvider(this).get(SearchViewModel::class.java) }
    lateinit var rv: RecyclerView
    lateinit var text_input: TextInputEditText
    lateinit var welcome_search: ImageView
    lateinit var not_found: View
    lateinit var connect_error: View
    lateinit var load: GifImageView
    val list = arrayListOf<ContentResponse>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        log("onCreateView() | ${vm.searchQueryDescription.value} | size ${vm.searchContent.value?.size}")
        val view = inflater.inflate(R.layout.fragment_search_description, container, false)
        rv = view.recycler_view
        text_input = view.text_input
        not_found = view.findViewById(R.id.not_found)
        connect_error = view.findViewById(R.id.error_load)
        connect_error.reload.setOnClickListener(this)
        load = view.findViewById(R.id.gif_load)
        welcome_search = view.welcome_image
        rv.adapter = SearchAdapter(list)
        rv.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        view.chip_one.setOnClickListener(this)
        view.chip_two.setOnClickListener(this)
        view.chip_three.setOnClickListener(this)
        view.chip_four.setOnClickListener(this)
        return view
    }

    @DelicateCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observerTextInput()
        observerSearch()
        vm.searchQueryDescription.value?.let {
            vm.searchQueryDescription.postValue(it)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    @DelicateCoroutinesApi
    fun observerTextInput(){
        text_input.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
                vm.searchQueryDescription.postValue(v.text.toString())
                return@setOnEditorActionListener true
            }
            false
        }
    }
    fun observerSearch(){
        vm.searchContent.observe(viewLifecycleOwner) {
            if (it.isEmpty()) vm.notFoundLiveData.postValue(true)
            else {
                clearVisibility()
                list.clear()
                list.addAll(it)
                rv.adapter?.notifyDataSetChanged()
                rv.visibility = View.VISIBLE
            }
        }
        vm.connectErrorLiveData.observe(viewLifecycleOwner){
            clearVisibility()
            connect_error.visibility = View.VISIBLE
        }
        vm.notFoundLiveData.observe(viewLifecycleOwner){
            clearVisibility()
            not_found.visibility = View.VISIBLE
        }
        vm.loadLiveData.observe(viewLifecycleOwner){
            clearVisibility()
            load.visibility = if(it) View.VISIBLE else View.GONE
        }
    }
    fun clearVisibility(){
        not_found.visibility = View.GONE
        connect_error.visibility = View.GONE
        load.visibility = View.GONE
        welcome_search.visibility = View.GONE
        rv.visibility = View.GONE
    }
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tv_title = itemView.title
        val char_category = itemView.char_category
        val descripton = itemView.description
        fun bind(content: ContentResponse){
            var title = "\uD83C\uDFAC ${content.ru_title}"
            if(content.year != null){
                title += " (${content.year})"
            }
            tv_title.text = title
            if(content.description != null) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    descripton.setText(Html.fromHtml(content.description, Html.FROM_HTML_MODE_LEGACY));
                } else {
                    descripton.setText(Html.fromHtml(content.description));
                }
            }

            char_category.text = when(content.contentType){
                "movie" -> "Ф"
                else -> "С"
            }
            itemView.setOnClickListener{
                val bundle = Bundle().apply {
                    putInt("id", content.id)
                }
                when(content.contentType){
                    "tv_series" -> {
                        (requireActivity() as MainActivity).navController.navigate(R.id.action_fragmentSearchDescription_to_fragmentAboutSerial, bundle)
                    }
                    "movie" -> {
                        (requireActivity() as MainActivity).navController.navigate(R.id.action_fragmentSearchDescription_to_fragmentAboutMovie, bundle)
                    }
                }
            }
        }
    }
    inner class SearchAdapter(val contentList: List<ContentResponse>): RecyclerView.Adapter<ViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = layoutInflater.inflate(R.layout.one_element_search_description, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(contentList[position])
        }

        override fun getItemCount(): Int {
            return contentList.size
        }

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.reload -> {
                vm.searchQueryDescription.postValue(vm.searchQueryDescription.value)
            }
            else -> {
                text_input.setText((v as Chip).text)
                vm.searchQueryDescription.postValue((v as Chip).text.toString())
            }
        }
    }


    fun log(s: String){
        Log.i("self-search", s)
    }

}