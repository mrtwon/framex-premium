package com.mrtwon.framex_premium.FragmentTop

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.mrtwon.framex_premium.Content.CollectionContentEnum
import com.mrtwon.framex_premium.Content.ContentTypeEnum
import com.mrtwon.framex_premium.Content.GenresEnum
import com.mrtwon.framex_premium.ContentResponse.ContentResponse
import com.mrtwon.framex_premium.GeneralVM

import kotlinx.coroutines.DelicateCoroutinesApi

class TopViewModel: GeneralVM() {
    val listLiveData =  MutableLiveData<List<ContentResponse>>()
    val connectErrorLiveData = MutableLiveData<Boolean>()
    val notFoundLiveData = MutableLiveData<Boolean>()
    val loadLiveData = MutableLiveData<Boolean>()

    private var currentPage = 1
    private var lastPage = 0
    private val MAX_PAGE = 5
    @DelicateCoroutinesApi
    fun getContentByGenresEnum(genres: GenresEnum, content: ContentTypeEnum){
        loadLiveData.postValue(true)
        model.getTopByGenresEnum(genres, content, 1,
            {
                loadLiveData.postValue(false)
                connectErrorLiveData.postValue(it)
            },
            {
                    loadLiveData.postValue(false)
                    listLiveData.postValue(it)
                    lastPage = if(it.isNotEmpty()) it[it.lastIndex].last_page else 0
                    log("lastPage ${lastPage}, content ${content.toString()}")
            }
        )
    }

    @DelicateCoroutinesApi
    fun getContentByCollectionEnum(collection: CollectionContentEnum, content: ContentTypeEnum){
        loadLiveData.postValue(true)
        model.getTopByCollectionEnum(collection, content, 1,
            {
                loadLiveData.postValue(false)
                lastPage = if(it.isNotEmpty()) it[it.lastIndex].last_page else 0
                listLiveData.postValue(it)
            },
            {
                loadLiveData.postValue(false)
                connectErrorLiveData.postValue(true)
            })
    }

    @DelicateCoroutinesApi
    fun giveNextPageCollection(collection: CollectionContentEnum, content: ContentTypeEnum){
        if(lastPage > currentPage && MAX_PAGE > currentPage){
            currentPage++

            model.getTopByCollectionEnum(collection, content, currentPage,
                {
                    listLiveData.postValue(it)
                },
                {
                    connectErrorLiveData.postValue(it)
                }
            )
        }
    }


    @DelicateCoroutinesApi
    fun giveNextPageGenres(genres: GenresEnum, content: ContentTypeEnum){
        if(lastPage > currentPage && MAX_PAGE > currentPage){
            log("next if, content ${content.toString()}")
            currentPage++

            model.getTopByGenresEnum(genres, content, currentPage,
                {
                    connectErrorLiveData.postValue(it)
                },
                {
                    listLiveData.postValue(it)
                }
            )
        }
    }

    private fun log(s: String){
        Log.i("self-top-vm",s)
    }
}