package com.mrtwon.testfirebase.paging.adapter.diff

import androidx.recyclerview.widget.DiffUtil
import com.mrtwon.framex_premium.domain.entity.ContentItemPage

class FirestoreDiff: DiffUtil.ItemCallback<ContentItemPage>() {
    override fun areItemsTheSame(oldItem: ContentItemPage, newItem: ContentItemPage): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: ContentItemPage,
        newItem: ContentItemPage
    ): Boolean {
        return oldItem == newItem
    }
}