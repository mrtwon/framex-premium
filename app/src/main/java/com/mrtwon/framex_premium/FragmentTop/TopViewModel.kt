package com.mrtwon.framex_premium.FragmentTop

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.startandroid.MyApplication
import com.mrtwon.framex_premium.Content.CollectionContentEnum
import com.mrtwon.framex_premium.Content.ContentTypeEnum
import com.mrtwon.framex_premium.Content.GenresEnum
import com.mrtwon.framex_premium.ContentResponse.Content
import com.mrtwon.framex_premium.Model.NewModel

import kotlinx.coroutines.DelicateCoroutinesApi

class TopViewModel: ViewModel() {
    var model: NewModel = MyApplication.getInstance.appComponents.getNewModel()
    val listLiveData =  MutableLiveData<List<Content>>()
    @DelicateCoroutinesApi
    fun getContentByGenresEnum(genres: GenresEnum, content: ContentTypeEnum){
        log("start vm function")
        model.getTopByGenresEnumTest(genres, content){
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