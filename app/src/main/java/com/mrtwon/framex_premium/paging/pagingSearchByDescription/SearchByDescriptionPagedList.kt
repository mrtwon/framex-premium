package com.mrtwon.testfirebase.paging.pagingSearchByDescription

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.mrtwon.framex_premium.domain.entity.ContentItemPage
import com.mrtwon.framex_premium.domain.usecase.GetContentByDescriptionPaging
import com.mrtwon.framex_premium.paging.DataSourceFactory
import com.mrtwon.testfirebase.paging.MainThreadExecutor
import com.mrtwon.testfirebase.paging.PagingCallback
import kotlinx.coroutines.CoroutineScope
import java.util.concurrent.Executors

class SearchByDescriptionPagedList(
    private val useCase: GetContentByDescriptionPaging,
    private val param: GetContentByDescriptionPaging.PrimaryParam,
    private val coroutineScope: CoroutineScope,
    private val pagingCallback: PagingCallback,
    private val retryLD: LiveData<Boolean>
) {
    fun create(): PagedList<ContentItemPage> {
        val storage = SearchByDescriptionStorage(
            useCase = useCase,
            param = param,
            scope = coroutineScope,
            pagingCallback = pagingCallback
        )
        val dataSource = DataSourceFactory(storage, retryLD).create()
        val config = PagedList.Config.Builder()
            .setPageSize(20)
            .setEnablePlaceholders(false)
            .build()
        val pagedList = PagedList.Builder(dataSource, config)
            .setNotifyExecutor(MainThreadExecutor())
            .setFetchExecutor(Executors.newSingleThreadExecutor())
            .build()
        return pagedList
    }
}