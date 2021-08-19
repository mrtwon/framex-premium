package com.mrtwon.framex_premium.FragmentAbout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mrtwon.framex_premium.ContentResponse.ContentResponse
import com.mrtwon.framex_premium.ContentResponse.Movie
import com.mrtwon.framex_premium.GeneralVM
import com.mrtwon.framex_premium.room.Favorite
import kotlinx.coroutines.DelicateCoroutinesApi

class AboutMovieViewModel: GeneralVM() {
    val contentData = MutableLiveData<Movie?>()
    val notFoundLiveData = MutableLiveData<Boolean>()
    val connectErrorLiveData = MutableLiveData<Boolean>()
    val loadLiveData = MutableLiveData<Boolean>()

    @DelicateCoroutinesApi
    fun getAbout(id: Int){
        loadLiveData.postValue(true)
        model.getAboutMovie(id,
            {
                loadLiveData.postValue(false)
                contentData.postValue(it)
            },
            {
                loadLiveData.postValue(false)
                connectErrorLiveData.postValue(it)
            }
        )
    }

    fun getFavoriteLiveData(id: Int): LiveData<Favorite>{
        return model.db.dao().getFavoriteIdLiveData(id)
    }

    fun favoriteAction(contentResponse: ContentResponse){
        model.favoriteAction(contentResponse)
    }
}