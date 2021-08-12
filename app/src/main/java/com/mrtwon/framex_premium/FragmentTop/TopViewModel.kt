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
    @DelicateCoroutinesApi
    fun getContentByGenresEnum(genres: GenresEnum, content: ContentTypeEnum){
        log("start vm function")
        model.getTopByGenresEnum(genres, content){
            listLiveData.postValue(it)
        }
    }
    @DelicateCoroutinesApi
    fun getContentByCollectionEnum(collection: CollectionContentEnum, content: ContentTypeEnum){
        /*model.getTopByCollectionEnum(collection, content){
            listLiveData.postValue(it)
        }*/
    }
    private fun log(s: String){
        Log.i("self-top-vm",s)
    }
}