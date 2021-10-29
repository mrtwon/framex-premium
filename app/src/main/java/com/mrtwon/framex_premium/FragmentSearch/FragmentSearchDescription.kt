package com.mrtwon.framex_premium.FragmentSearch

import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.animation.addListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputEditText
import com.mrtwon.framex_premium.ContentResponse.ContentResponse
import com.mrtwon.framex_premium.MainActivity
import com.mrtwon.framex_premium.R
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.animators.FadeInAnimator
import kotlinx.android.synthetic.main.fragment_about_movie.view.*
import kotlinx.android.synthetic.main.fragment_search.view.recycler_view
import kotlinx.android.synthetic.main.fragment_search.view.text_input
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
    lateinit var loadInRv: GifImageView
    lateinit var layout_rv: LinearLayout
    lateinit var hide_help: ImageView
    lateinit var help_layout: LinearLayout
    val list = arrayListOf<ContentResponse>()
    val saveState = Bundle()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        log("onCreateView()")
        val view = inflater.inflate(R.layout.fragment_search_description, container, false)
        rv = view.recycler_view
        text_input = view.text_input
        not_found = view.findViewById(R.id.not_found)
        connect_error = view.findViewById(R.id.error_load)
        connect_error.reload.setOnClickListener(this)
        loadInRv = view.findViewById(R.id.gif_load_in_rv)
        load = view.findViewById(R.id.gif_load)
        layout_rv = view.findViewById(R.id.layout_rv)
        hide_help = view.findViewById(R.id.hide_help)
        help_layout = view.findViewById(R.id.help_layout)
        welcome_search = view.welcome_image
        rv.itemAnimator = FadeInAnimator()
        rv.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        rv.adapter = ScaleInAnimationAdapter(AlphaInAnimationAdapter(SearchAdapter(list))).apply {
            setDuration(200)
            setFirstOnly(false)
        }
        hide_help.setOnClickListener{ hideHelpLayout() }
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
        checkState()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun checkState(){
        if(list.isNotEmpty()){
            clearVisibility()
            layout_rv.visibility = View.VISIBLE
        }
    }

    @DelicateCoroutinesApi
    fun observerTextInput(){
        text_input.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT){
                list.clear()
                rv.adapter?.notifyDataSetChanged()
                vm.searchQueryDescription.postValue(v.text.toString())
                return@setOnEditorActionListener true
            }
            false
        }
    }
    private fun observerSearch(){
        vm.connectErrorLiveData.observe(viewLifecycleOwner){
            if(it) clearVisibility()
            connect_error.visibility = if(it) View.VISIBLE else View.GONE
        }
        vm.notFoundLiveData.observe(viewLifecycleOwner){
            if(it) clearVisibility()
            not_found.visibility = if(it) View.VISIBLE else View.GONE
        }
        vm.loadLiveData.observe(viewLifecycleOwner) {
            if (list.isNotEmpty()) {
                loadInRv.visibility = if (it) View.VISIBLE else View.GONE
            } else {
                if (it) clearVisibility()
                load.visibility = if (it) View.VISIBLE else View.GONE
            }
        }
        vm.searchContent.observe(viewLifecycleOwner){
            if(it == null) return@observe
            log("observerSearch()")
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
    private fun clearVisibility(){
        not_found.visibility = View.GONE
        connect_error.visibility = View.GONE
        load.visibility = View.GONE
        welcome_search.visibility = View.GONE
        layout_rv.visibility = View.GONE
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
            if(position == contentList.size-1){
                vm.nextPageDescription()
            }
            log("position ${position}")
            holder.bind(contentList[position])
        }

        override fun getItemCount(): Int {
            log("rv size = ${contentList.size}")
            return contentList.size
        }

    }


    override fun onDestroyView() {
        rv.layoutManager?.onSaveInstanceState()?.let {
            log("save state")
            saveState.putParcelable("rv", it)
        }
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

    fun hideHelpLayout(){
        ObjectAnimator.ofFloat(help_layout, "alpha", 0f).apply {
            duration = 300
            start()
        }.addListener(onEnd = {
            help_layout.visibility = View.GONE
        })
    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.reload -> {
                if(list.isEmpty())
                    vm.searchQueryDescription.postValue(vm.searchQueryDescription.value)
                else
                    vm.nextPageDescription(vm.searchQueryDescription.value)
            }
            else -> {
                list.clear()
                rv.adapter?.notifyDataSetChanged()
                text_input.setText((v as Chip).text)
                vm.searchQueryDescription.postValue((v as Chip).text.toString())
            }
        }
    }


    fun log(s: String){
        Log.i("self-search", s)
    }

}