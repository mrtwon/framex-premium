package com.mrtwon.framex_premium

import androidx.lifecycle.MutableLiveData
import com.mrtwon.framex_premium.room.Content
import com.mrtwon.framex_premium.room.Recent

class MainViewModel: GeneralVM() {
    val listRecent = MutableLiveData<List<Recent>>()

    fun getRecent(){
        model.getRecentList {
            listRecent.postValue(it)
        }
    }
}