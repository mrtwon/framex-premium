package com.mrtwon.framex_premium.FragmentAbout

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.startandroid.MyApplication
import com.mrtwon.framex_premium.ContentResponse.Movie
import com.mrtwon.framex_premium.GeneralVM
import com.mrtwon.framex_premium.room.MovieWithGenres
import kotlinx.coroutines.DelicateCoroutinesApi

class AboutMovieViewModel: ViewModel() {
    val old_model = MyApplication.getInstance.appComponents.getModel()
    val model = MyApplication.getInstance.appComponents.getNewModel()
    val contentData = MutableLiveData<Movie>()
    val isFavoriteBoolean = MutableLiveData<Boolean>()
    @DelicateCoroutinesApi
    fun getAbout(id: Int){
        model.getAboutMovie(id){
            contentData.postValue(it)
        }
    }
    fun addFavorite(id: Int, contentType: String = "movie"){
        old_model.addFavorite(id, contentType)
    }
    fun deleteFavorite(id: Int, contentType: String = "movie"){
        old_model.removeFavorite(id, contentType)
    }
    fun isFavorite(id: Int, contentType: String = "movie"){
        old_model.isFavorite(id, contentType){
            isFavoriteBoolean.postValue(it)
        }
    }
}