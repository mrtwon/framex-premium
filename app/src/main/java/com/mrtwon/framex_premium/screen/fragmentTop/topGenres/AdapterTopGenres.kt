package com.mrtwon.framex_premium.screen.fragmentTop.topGenres

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.mrtwon.framex_premium.R
import com.mrtwon.framex_premium.domain.entity.ContentItemPage
import com.mrtwon.framex_premium.screen.fragmentTop.TopOpenCallback
import com.mrtwon.testfirebase.paging.adapter.diff.FirestoreDiff
import com.squareup.picasso.Picasso

class AdapterTopGenres(private val diff: FirestoreDiff, private val callback: TopOpenCallback, private val picasso: Picasso)
    : PagedListAdapter<ContentItemPage, ViewHolderTopGenres>(diff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTopGenres {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.one_top_element, parent, false)
        return ViewHolderTopGenres(itemView = view, callback = callback, picasso = picasso)
    }

    override fun onBindViewHolder(holder: ViewHolderTopGenres, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }
}