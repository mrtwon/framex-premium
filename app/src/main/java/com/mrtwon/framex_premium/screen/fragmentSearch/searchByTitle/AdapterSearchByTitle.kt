package com.mrtwon.framex_premium.screen.fragmentSearch.searchByTitle

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.mrtwon.framex_premium.R
import com.mrtwon.framex_premium.domain.entity.ContentItemPage
import com.mrtwon.framex_premium.screen.fragmentSearch.SearchCallback
import com.mrtwon.testfirebase.paging.adapter.diff.FirestoreDiff

class AdapterSearchByTitle(private val diff: FirestoreDiff, private val callback: SearchCallback)
    : PagedListAdapter<ContentItemPage,  SearchByTitleVH>(diff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchByTitleVH {
        Log.i("self-test-search","onCreateViewHolder()")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.one_element_search, parent, false)
        return SearchByTitleVH(view = view, callback = callback)
    }

    override fun onBindViewHolder(holder: SearchByTitleVH, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }
}