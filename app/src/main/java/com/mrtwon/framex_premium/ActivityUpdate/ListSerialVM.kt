package com.mrtwon.framex_premium.ActivityUpdate

import androidx.lifecycle.MutableLiveData
import com.mrtwon.framex_premium.GeneralVM
import com.mrtwon.framex_premium.room.Serial

class ListSerialVM: GeneralVM() {
    val serialList = MutableLiveData<List<Serial>>()
    val visibilityPb = MutableLiveData<Boolean>()
    val noConnect = MutableLiveData<Boolean>()
    fun searchSerial(query: String){
        visibilityPb.postValue(true)
        model.searchSerial(query, {
            visibilityPb.postValue(false)
            serialList.postValue(it)
        },{
            noConnect.postValue(it)
        })
    }
}