package com.mrtwon.framex_premium.screen.fragmentTop

import android.content.res.Resources
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.mrtwon.framex_premium.R
import com.mrtwon.framex_premium.domain.entity.enum.ContentEnum

class TabLayoutAdapter(fm: FragmentManager, res: Resources, val listFragment: List<Fragment>): FragmentPagerAdapter(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    init {
        listFragment.forEach { fragment ->
            //Log.i("self-last","fragment toString ${fragment.toString()} ${fragment.hashCode()}")
            fragment.arguments?.getString("contentEnum")?.let { str ->
                Log.i("self-last", "init tabL ${ContentEnum.valueOf(str).type} [$str]")
            }
            fragment.arguments?.getInt("hash")?.let {
                Log.i("self-last","init tabL hash - $it")
            }
        }
    }
    private val titleList = arrayListOf<String>(res.getString(R.string.tab_movie),res.getString(R.string.tab_serial))
    override fun getCount(): Int {
        return titleList.size
    }

    override fun getItem(position: Int): Fragment {
        listFragment.get(position).arguments?.getString("contentEnum")?.let {
            Log.i("self-main","tabLayoutAdapter[$position] ${ContentEnum.valueOf(it).type}[$it]")
        }
        return listFragment[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }

}