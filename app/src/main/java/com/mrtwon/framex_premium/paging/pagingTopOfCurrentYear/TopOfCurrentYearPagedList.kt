package com.mrtwon.testfirebase.paging.pagingTopOfCurrentYear

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.mrtwon.framex_premium.domain.entity.ContentItemPage
import com.mrtwon.framex_premium.domain.usecase.GetTopContentByYearPaging
import com.mrtwon.framex_premium.paging.DataSourceFactory
import com.mrtwon.framex_premium.paging.pagingTopOfCurrentYear.TopOfCurrentYearStorage
import com.mrtwon.testfirebase.paging.MainThreadExecutor
import com.mrtwon.testfirebase.paging.PagingCallback
import kotlinx.coroutines.CoroutineScope
import java.util.concurrent.Executors

class TopOfCurrentYearPagedList(
    private val useCase: GetTopContentByYearPaging,
    private val param: GetTopContentByYearPaging.PrimaryParams,
    private val coroutineScope: CoroutineScope,
    private val callback: PagingCallback,
    private val retryLiveData: LiveData<Boolean>
) {
    fun create(): PagedList<ContentItemPage> {
        val storage = TopOfCurrentYearStorage(useCase, param, coroutineScope, callback)
        val dataSource = DataSourceFactory(storage, retryLiveData).create()
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