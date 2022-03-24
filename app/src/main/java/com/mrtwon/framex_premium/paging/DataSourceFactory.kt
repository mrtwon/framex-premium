package com.mrtwon.framex_premium.paging

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.mrtwon.framex_premium.domain.entity.ContentItemPage
import com.mrtwon.testfirebase.paging.FirebaseDataSource
import com.mrtwon.testfirebase.paging.FirebaseStorage

class DataSourceFactory constructor(private val storage: FirebaseStorage<ContentItemPage, ContentItemPage>, private val retryLD: LiveData<Boolean>): DataSource.Factory<ContentItemPage, ContentItemPage>() {

    override fun create(): DataSource<ContentItemPage, ContentItemPage> {
        return FirebaseDataSource(storage = storage, retryLD = retryLD)
    }
}