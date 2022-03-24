package com.mrtwon.testfirebase.paging

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.ItemKeyedDataSource
import com.google.firebase.firestore.DocumentSnapshot
import com.mrtwon.framex_premium.domain.entity.ContentItemPage

class FirebaseDataSource(private val storage: FirebaseStorage<ContentItemPage, ContentItemPage>, private val retryLD: LiveData<Boolean>)
    : ItemKeyedDataSource<ContentItemPage, ContentItemPage>() {
    override fun getKey(item: ContentItemPage): ContentItemPage {
        return item
    }
    private var retryCallback: LoadCallback<ContentItemPage>? = null
    private var retryInitCallback: LoadInitialCallback<ContentItemPage>? = null
    private var retryParam: LoadParams<ContentItemPage>? = null
    private var retryInitParam: LoadInitialParams<ContentItemPage>? = null

    init {
        retryLD.observeForever {
            Log.i("self-retry","retry $it")
            if(!it) return@observeForever
            if(retryInitCallback != null && retryInitParam != null) {
                loadInitial(retryInitParam!!, retryInitCallback!!)
            }
            else if(retryCallback != null && retryParam != null){
                loadAfter(retryParam!!, retryCallback!!)
            }
        }
    }

    override fun loadInitial(
        params: LoadInitialParams<ContentItemPage>,
        callback: LoadInitialCallback<ContentItemPage>
    ){
        retryInitCallback = callback
        retryInitParam = params
        Log.i("self-retry","loadFirst ${params.requestedLoadSize}")
        storage.first(params.requestedLoadSize.toLong()){
            Log.i("self-main","FirebaseDataSource - loadInitial loadSize = ${params.requestedLoadSize}")
            Log.i("self-main","first Result size ${it.size}")
            callback.onResult(it)
            retryInitCallback = null
            retryInitParam = null
        }
    }

    override fun loadAfter(
        params: LoadParams<ContentItemPage>,
        callback: LoadCallback<ContentItemPage>
    ) {
        retryParam = params
        retryCallback = callback
        Log.i("self-retry","loadAfter ${params.requestedLoadSize}")
        storage.after(params.key, params.requestedLoadSize.toLong()){
            retryCallback = null
            retryParam = null
            callback.onResult(it)
        }
    }

    override fun loadBefore(
        params: LoadParams<ContentItemPage>,
        callback: LoadCallback<ContentItemPage>
    ) {
        storage.before(params.key, params.requestedLoadSize.toLong()){
            callback.onResult(it)
        }
    }

}