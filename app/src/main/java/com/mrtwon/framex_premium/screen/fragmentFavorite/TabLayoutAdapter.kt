package com.mrtwon.framex_premium.screen.fragmentFavorite

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.mrtwon.framex_premium.R

class TabLayoutAdapter(fm: FragmentManager, res: Resources): FragmentPagerAdapter(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    val titleList = arrayListOf<String>(
        res.getString(R.string.tab_favorite),
        res.getString(R.string.tab_recent))

    val fragmentList = arrayListOf<Fragment>(
        FragmentFavorite())

    override fun getCount(): Int {
        return titleList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }

}