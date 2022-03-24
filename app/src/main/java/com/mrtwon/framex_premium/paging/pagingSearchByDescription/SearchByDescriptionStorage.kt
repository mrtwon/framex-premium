package com.mrtwon.testfirebase.paging.pagingSearchByDescription

import com.google.firebase.firestore.DocumentSnapshot
import com.mrtwon.framex_premium.domain.entity.ContentItemPage
import com.mrtwon.framex_premium.domain.entity.enum.StartPosition
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.usecase.GetContentByDescriptionPaging
import com.mrtwon.testfirebase.paging.FirebaseStorage
import com.mrtwon.testfirebase.paging.PagingCallback
import kotlinx.coroutines.CoroutineScope

class SearchByDescriptionStorage(
    private val useCase: GetContentByDescriptionPaging,
    private val param: GetContentByDescriptionPaging.PrimaryParam,
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
            GetContentByDescriptionPaging.Params(
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
            GetContentByDescriptionPaging.Params(
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
            GetContentByDescriptionPaging.Params(
                primaryParam = param,
                itemKey = null,
                limit = limit,
                position = StartPosition.After), scope = scope
        ){
            pagingCallback.onFirstLoad(false)
            it.fold(
                { failure ->
                    pagingCallback.onError(failure)
                },
                { contentItemList ->
                    callback(contentItemList)
                    if(contentItemList.isEmpty()){
                        pagingCallback.onNotFound(true)
                        pagingCallback.onNotFound(false)
                    }else{
                        pagingCallback.onSuccessful(true)
                        pagingCallback.onSuccessful(false)
                    }
                }
            )
        }
    }
}