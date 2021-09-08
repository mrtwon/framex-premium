package com.mrtwon.framex_premium.FragmentTop

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.dueeeke.tablayout.SlidingTabLayout
import com.mrtwon.framex_premium.Content.ParcelableEnum
import com.mrtwon.framex_premium.R
import kotlinx.android.synthetic.main.fragment_top_content.view.*

class FragmentTop: Fragment(), View.OnClickListener {
    val mainTopVM: MainTopVM by lazy { ViewModelProvider(this).get(MainTopVM::class.java) }
    lateinit var view_pager: ViewPager
    lateinit var tab_layout: SlidingTabLayout
    lateinit var btn_filter: ImageButton
    var imageResId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        imageResId = arguments?.getInt("img_resource")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_top_content, container, false)
        btn_filter = view.findViewById(R.id.btn_filter)
        view_pager = view.view_pager
        tab_layout = view.tab_layout
        imageResId?.let {
            view.top_image.setImageResource(it)
        }
        view_pager.adapter = TabLayoutAdapter(childFragmentManager, resources, requireArguments())
        tab_layout.setViewPager(view_pager)
        btn_filter.setOnClickListener(this)
        return view
    }

    companion object{
        fun instance(): Fragment{
            return FragmentTop()
        }
    }



    private fun showFilterSetting(){
        val arguments = requireArguments()
        val genres = arguments.getParcelable<ParcelableEnum>("genres_enum")?.genresEnum
        val collection = arguments.getParcelable<ParcelableEnum>("collection_enum")?.collectionEnum
        if(genres != null){
               FilterRatingFragment().show(childFragmentManager, "filter rating fragment")
        }
        if(collection != null){
            FilterFragment().show(childFragmentManager, "filter fragment")
        }
    }

    override fun onDestroy() {
        Log.i("self-top-content","onDestroy()")
        super.onDestroy()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_filter -> {
                showFilterSetting()
            }
        }
    }
}