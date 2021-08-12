package com.mrtwon.framex_premium.FragmentAbout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.startandroid.MyApplication
import com.mrtwon.framex_premium.ContentResponse.ContentResponse
import com.mrtwon.framex_premium.ContentResponse.Serial
import com.mrtwon.framex_premium.GeneralVM
import com.mrtwon.framex_premium.room.Favorite
import com.mrtwon.framex_premium.room.SerialWithGenres
import com.mrtwon.framex_premium.room.Subscription
import kotlinx.coroutines.DelicateCoroutinesApi

class AboutSerialViewModel: GeneralVM() {
    lateinit var isSubscriptionLiveData: LiveData<Subscription>
    val contentData = MutableLiveData<Serial?>()

    @DelicateCoroutinesApi
    fun getAbout(id: Int){
        model.getAboutSerial(id){
            contentData.postValue(it)
        }
    }

    fun getFavoriteLiveData(id: Int): LiveData<Favorite>{
        return model.db.dao().getFavoriteIdLiveData(id)
    }

    fun favoriteAction(contentResponse: ContentResponse){
        model.favoriteAction(contentResponse)
    }

    fun initSubscriptionLiveData(id: Int): LiveData<Subscription>{
        return model.getSubscriptionByIdLiveData(id)
    }
    fun subscriptionAction(id: Int){
        model.subscriptionIf(id)
    }
}