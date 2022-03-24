package com.mrtwon.framex_premium.screen.fragmentTop

import FilterTopGenres
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
import com.mrtwon.framex_premium.R
import com.mrtwon.framex_premium.domain.entity.Content
import com.mrtwon.framex_premium.domain.entity.enum.ContentEnum
import com.mrtwon.framex_premium.domain.entity.enum.GenresEnum
import com.mrtwon.framex_premium.screen.fragmentTop.topCurrentYear.FilterTopCurrentYear
import com.mrtwon.framex_premium.screen.fragmentTop.topCurrentYear.FragmentTopCurrentYear
import com.mrtwon.framex_premium.screen.fragmentTop.topGenres.FragmentTopGenres
import kotlinx.android.synthetic.main.fragment_top_content.view.*
import java.util.*

class FragmentTop: Fragment(), View.OnClickListener {
    private val mainTopVM: MainTopVM by lazy { ViewModelProvider(this).get(MainTopVM::class.java) }
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: SlidingTabLayout
    private lateinit var btnFilter: ImageButton
    private lateinit var topByEnum: TopByEnum
    private var imageResId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        arguments?.getString("topByEnum")?.let {
            val topByEnum = TopByEnum.valueOf(it)
            this.topByEnum = topByEnum
            imageResId = topByEnum.imageRes
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_top_content, container, false)
        btnFilter = view.findViewById(R.id.btn_filter)
        viewPager = view.view_pager
        tabLayout = view.tab_layout
        imageResId?.let { view.top_image.setImageResource(it) }
        viewPager.adapter = TabLayoutAdapter(childFragmentManager, resources, getFragmentList(topByEnum, requireArguments()))
        tabLayout.setViewPager(viewPager)
        btnFilter.setOnClickListener(this)
        return view
    }

    companion object{
        fun instance(): Fragment{
            return FragmentTop()
        }
    }

    private fun getFragmentList(topBy: TopByEnum, bundle: Bundle): List<Fragment>{
        return when(topBy){
            TopByEnum.CurrentYear -> arrayListOf(
                FragmentTopCurrentYear.instance(ContentEnum.Movie),
                FragmentTopCurrentYear.instance(ContentEnum.Serial)
            )
            else -> arrayListOf(
                FragmentTopGenres.instance(ContentEnum.Movie, GenresEnum.valueOf(bundle.getString("genresEnum")!!)),
                FragmentTopGenres.instance(ContentEnum.Serial, GenresEnum.valueOf(bundle.getString("genresEnum")!!))
            )
        }
    }



    private fun showFilterSetting(){
        if(topByEnum == TopByEnum.CurrentYear){
            mainTopVM.filterCurrentYearLD.value?.let {
                FilterTopCurrentYear()
                    .show(childFragmentManager, "filter fragment")
            }
        }else{
            mainTopVM.filterGenresLD.value?.let {
                FilterTopGenres()
                    .show(childFragmentManager, "filter fragment")
            }
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