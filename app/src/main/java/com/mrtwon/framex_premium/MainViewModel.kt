package com.mrtwon.framex_premium

import androidx.lifecycle.MutableLiveData
import com.mrtwon.framex_premium.room.Content
import com.mrtwon.framex_premium.room.DatabaseSize

class MainViewModel: GeneralVM() {
    val listRecent = MutableLiveData<List<Content>>()
    val dbSize = MutableLiveData<DatabaseSize>()
    fun getRecent(){
        model.getRecentContent {
            listRecent.postValue(it)
        }
    }
    fun getDatabaseSize(){
        model.getSizeDatabase {
            dbSize.postValue(it)
        }
    }

}