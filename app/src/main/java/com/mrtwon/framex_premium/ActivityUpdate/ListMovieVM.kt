package com.mrtwon.framex_premium.ActivityUpdate

import androidx.lifecycle.MutableLiveData
import com.mrtwon.framex_premium.GeneralVM
import com.mrtwon.framex_premium.room.Movie

class ListMovieVM: GeneralVM() {
    //val modelApi = ModelApi()
    val movieList = MutableLiveData<List<Movie>>()
    val visibilityPb = MutableLiveData<Boolean>()
    val noConnect = MutableLiveData<Boolean>()
    fun searchMovie(query: String){
        visibilityPb.postValue(true)
        model.searchMovie(query, {
            visibilityPb.postValue(false)
            movieList.postValue(it)
        },{
            noConnect.postValue(it)
        })
    }
}