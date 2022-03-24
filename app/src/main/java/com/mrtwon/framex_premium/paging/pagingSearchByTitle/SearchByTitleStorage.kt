package com.mrtwon.testfirebase.paging.pagingSearchByTitle

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.mrtwon.framex_premium.domain.entity.ContentItemPage
import com.mrtwon.framex_premium.domain.entity.enum.StartPosition
import com.mrtwon.framex_premium.domain.usecase.GetContentByTitlePaging
import com.mrtwon.testfirebase.paging.FirebaseStorage
import com.mrtwon.testfirebase.paging.PagingCallback
import kotlinx.coroutines.CoroutineScope

class SearchByTitleStorage(
    private val useCase: GetContentByTitlePaging,
    private val param: GetContentByTitlePaging.PrimaryParam,
    private val scope: CoroutineScope,
    private val pagingCallback: PagingCallback
): FirebaseStorage<ContentItemPage, ContentItemPage> {
    override fun after(
        document: ContentItemPage,
        limit: Long,
        callback: (List<ContentItemPage>) -> Unit
    ) {
        pagingCallback.onLoad(true)
        useCase(
            params = GetContentByTitlePaging.Params(
                primaryParam = param,
                itemKey = document,
                limit = limit,
                position = StartPosition.After), scope = scope
        ){
            pagingCallback.onLoad(false)
            it.fold(
                { failure ->
                    pagingCallback.onError(failure)
                },
                { contentItemList ->
                    callback(contentItemList)
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
            params = GetContentByTitlePaging.Params(
                primaryParam = param,
                itemKey = document,
                limit = limit,
                position = StartPosition.Before), scope = scope
        ){
            it.fold(
                { failure ->
                    pagingCallback.onError(failure)
                },
                { contentItemList ->
                    callback(contentItemList)
                }
            )
        }
    }

    override fun first(limit: Long, callback: (List<ContentItemPage>) -> Unit) {
        pagingCallback.onFirstLoad(true)
        useCase(
            params = GetContentByTitlePaging.Params(
                primaryParam = param,
                itemKey = null,
                limit = limit,
                position = StartPosition.After), scope = scope
        ){
            pagingCallback.onFirstLoad(false)
            it.fold(
                { failure ->
                    Log.i("self-storage-search", "failure")
                    pagingCallback.onError(failure = failure)
                },
                { contentItemList ->
                    Log.i("self-storage-search", "size first ${contentItemList.size}")
                    callback(contentItemList)
                    if(contentItemList.isEmpty()){
                        pagingCallback.onNotFound(true)
                        pagingCallback.onNotFound(false)
                    }else{
                        pagingCallback.onSuccessful(true)
                    }
                }
            )
        }
    }
}