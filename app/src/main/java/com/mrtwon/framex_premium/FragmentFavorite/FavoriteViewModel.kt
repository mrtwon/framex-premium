package com.mrtwon.framex_premium.FragmentFavorite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mrtwon.framex_premium.GeneralVM
import com.mrtwon.framex_premium.room.Content
import com.mrtwon.framex_premium.room.Favorite

class FavoriteViewModel: GeneralVM() {

    val favoriteLiveData: LiveData<List<Favorite>> = model.db.dao().getFavoriteLiveData()
    /*val favoriteList = MutableLiveData<List<Favorite>>()

    fun getFavorite(){
        Log.i("self-favorite","getContent()")
        model.getFavorite {
            favoriteList.postValue(it)
        }
    }*/

    fun removeFavorite(favorite: Favorite){
        model.removeFavorite(favorite)
    }

}







