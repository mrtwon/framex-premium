package com.mrtwon.framex_premium.paging.pagingTopByGenres

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.mrtwon.framex_premium.data.extenstion.toContent
import com.mrtwon.framex_premium.domain.entity.ContentItemPage
import com.mrtwon.framex_premium.domain.entity.enum.StartPosition
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.usecase.GetTopContentByGenresPaging
import com.mrtwon.testfirebase.paging.FirebaseStorage
import com.mrtwon.testfirebase.paging.PagingCallback
import kotlinx.coroutines.CoroutineScope

class TopByGenresStorage(
    private val useCase: GetTopContentByGenresPaging,
    private val param: GetTopContentByGenresPaging.PrimaryParams,
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
        if(countDocument >= maxDocumentCount){
            callback(arrayListOf())
            return
        }
        pagingCallback.onLoad(true)
        useCase(
            params = GetTopContentByGenresPaging.Params(
                primaryParam = param,
                position = StartPosition.After,
                itemKey = document,
                limit = limit), scope = scope){
            pagingCallback.onLoad(false)
            Log.i("self-top-genres","finish")
            it.fold(
                { failure ->
                    pagingCallback.onError(failure)
                    Log.i("self-top-genres","failure")
                },
                { contentItemList ->
                    Log.i("self-debug-firestore","[after] input limit $limit | result size ${contentItemList.size}")
                    Log.i("self-top-genres","result, size = ${contentItemList.size}")
                    callback(contentItemList)
                    // 848
                    updateDocumentCount(contentItemList.size)
                    pagingCallback.onSuccessful(true)
                }
            )
        }
    }

    override fun before(
        document: ContentItemPage,
        limit: Long,
        callback: (List<ContentItemPage>) -> Unit
    ) {
        useCase(
            params = GetTopContentByGenresPaging.Params(
                primaryParam = param,
                position = StartPosition.Before,
                itemKey = document,
                limit = limit), scope = scope){
            it.fold(
                { failure ->
                    pagingCallback.onError(failure)
                },
                { contentItemList ->
                    Log.i("self-debug-firestore","[before]input limit $limit | result size ${contentItemList.size}")
                    callback(contentItemList)
                    updateDocumentCount(contentItemList.size)
                }
            )
        }
    }

    override fun first(limit: Long, callback: (List<ContentItemPage>) -> Unit) {
        if(countDocument >= maxDocumentCount){
            callback(arrayListOf())
            return
        }
        pagingCallback.onFirstLoad(true)
        useCase(
            params = GetTopContentByGenresPaging.Params(
                primaryParam = param,
                position = StartPosition.After,
                itemKey = null,
                limit = limit), scope = scope){
            pagingCallback.onFirstLoad(false)
            it.fold(
                { failure ->
                    pagingCallback.onError(failure)
                },
                { contentItemList ->
                    Log.i("self-top-genres", "result first size ${contentItemList.size}")
                    Log.i("self-debug-firestore","[first ]input limit $limit | result size ${contentItemList.size}")
                    callback(contentItemList)
                    if(contentItemList.isEmpty()){
                        pagingCallback.onNotFound(true)
                        pagingCallback.onNotFound(false)
                    }else{
                        Log.i("self-top-genres","callback on onSuccessfull")
                        pagingCallback.onSuccessful(true)
                        pagingCallback.onSuccessful(false)
                        updateDocumentCount(contentItemList.size)
                    }
                }
            )
        }
    }

    private fun updateDocumentCount(size: Int){
        countDocument += size
    }
}