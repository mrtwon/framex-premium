package com.mrtwon.framex_premium.FragmentAbout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.startandroid.MyApplication
import com.mrtwon.framex_premium.ContentResponse.ContentResponse
import com.mrtwon.framex_premium.ContentResponse.Movie
import com.mrtwon.framex_premium.GeneralVM
import com.mrtwon.framex_premium.room.Favorite
import com.mrtwon.framex_premium.room.MovieWithGenres
import kotlinx.coroutines.DelicateCoroutinesApi

class AboutMovieViewModel: GeneralVM() {
    val contentData = MutableLiveData<Movie>()
    @DelicateCoroutinesApi
    fun getAbout(id: Int){
        model.getAboutMovie(id){
            contentData.postValue(it)
        }
    }

    fun getFavoriteLiveData(id: Int): LiveData<Favorite>{
        return model.db.dao().getFavoriteIdLiveData(id)
    }

    fun favoriteAction(contentResponse: ContentResponse){
        model.favoriteAction(contentResponse)
    }
}