package com.mrtwon.framex_premium.paging.pagingTopOfCurrentYear

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.mrtwon.framex_premium.domain.entity.ContentItemPage
import com.mrtwon.framex_premium.domain.entity.enum.StartPosition
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.usecase.GetTopContentByYearPaging
import com.mrtwon.testfirebase.paging.FirebaseStorage
import com.mrtwon.testfirebase.paging.PagingCallback
import kotlinx.coroutines.CoroutineScope

class TopOfCurrentYearStorage(
    private val useCase: GetTopContentByYearPaging,
    private val param: GetTopContentByYearPaging.PrimaryParams,
    private val scope: CoroutineScope,
    private val pagingCallback: PagingCallback
    ): FirebaseStorage<ContentItemPage, ContentItemPage> {

    private var countDocument = 0
    private val maxDocumentCount = 100
    override fun after(
        document: ContentItemPage,
        limit: Long,
        callback: (List<ContentItemPage>) -> Unit
    ) {
        Log.i("self-main", "after - limit $limit")
        if(countDocument >= maxDocumentCount){
            callback(arrayListOf())
            return
        }
        pagingCallback.onLoad(true)
        useCase(params = GetTopContentByYearPaging.Params(
            primaryParam = param,
            itemKey = document,
            position = StartPosition.After,
            limit = limit),
            scope = scope
        ) {
            pagingCallback.onLoad(false)
            it.fold(
                { failure -> pagingCallback.onError(failure) },
                { result ->
                    callback(result)
                    pagingCallback.onSuccessful(true)
                    updateDocumentCount(result.size)
                }
            )
        }
    }

    override fun before(
        document: ContentItemPage,
        limit: Long,
        callback: (List<ContentItemPage>) -> Unit
    ) {
        Log.i("self-main", "before - limit $limit")
        useCase(
            params = GetTopContentByYearPaging.Params(
                primaryParam = param,
                itemKey = document,
                position = StartPosition.Before,
                limit = limit), scope = scope
        ){
            it.fold(
                { failure -> pagingCallback.onError(failure) },
                { result ->
                    Log.i("self-main", "before, size ${result.size}")
                    callback(result)
                }
            )
        }

    }

    override fun first(limit: Long, callback: (List<ContentItemPage>) -> Unit) {
        Log.i("self-main", "first - limit $limit")
        if(countDocument >= maxDocumentCount){
            callback(arrayListOf())
            return
        }
        pagingCallback.onFirstLoad(true)
        useCase(params = GetTopContentByYearPaging.Params(
            primaryParam = param,
            itemKey = null,
            position = StartPosition.After,
            limit = limit), scope = scope
        ){
            pagingCallback.onFirstLoad(false)
            it.fold(
                { failure -> pagingCallback.onError(failure) },
                { result ->
                    Log.i("self-debug","topOfCurY first ${result.size}")
                    callback(result)
                    if(result.isEmpty()){
                        pagingCallback.onNotFound(true)
                        pagingCallback.onNotFound(false)
                    }else{
                        pagingCallback.onSuccessful(true)
                        pagingCallback.onSuccessful(false)
                        updateDocumentCount(result.size)
                    }
                }
            )
        }
    }

    private fun updateDocumentCount(size: Int){
        countDocument += size
    }


}