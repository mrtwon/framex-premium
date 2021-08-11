package com.mrtwon.framex_premium.FragmentFavorite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mrtwon.framex_premium.GeneralVM
import com.mrtwon.framex_premium.room.Content
import com.mrtwon.framex_premium.room.Favorite

class FavoriteViewModel: GeneralVM() {

    private val favoriteLiveData: LiveData<List<Favorite>> = model.db.dao().getFavoriteLiveData()
    val contentList = MutableLiveData<List<Content>>()

    init {
        favoriteLiveData.observeForever {
            getContent()
        }
    }
    fun getContent(){
        Log.i("self-favorite","getContent()")
        model.getFavorite {
            contentList.postValue(it)
        }
    }

    fun removeFavorite(id: Int, contentType: String){
        model.removeFavorite(id, contentType)
    }

}







