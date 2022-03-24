package com.mrtwon.framex_premium.screen.fragmentSearch.searchByDescription

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.mrtwon.framex_premium.R
import com.mrtwon.framex_premium.domain.entity.ContentItemPage
import com.mrtwon.framex_premium.screen.fragmentSearch.SearchCallback
import com.mrtwon.testfirebase.paging.adapter.diff.FirestoreDiff

class AdapterSearchByDescription(private val diff: FirestoreDiff, private val callback: SearchCallback)
    : PagedListAdapter<ContentItemPage, SearchByDescriptionVH>(diff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchByDescriptionVH {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.one_element_search_description, parent, false
            )
        return SearchByDescriptionVH(view = view, callback = callback)
    }

    override fun onBindViewHolder(holder: SearchByDescriptionVH, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }
}