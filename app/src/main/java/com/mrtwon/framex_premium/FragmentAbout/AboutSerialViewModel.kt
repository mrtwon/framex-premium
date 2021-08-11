package com.mrtwon.framex_premium.FragmentAbout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.startandroid.MyApplication
import com.mrtwon.framex_premium.ContentResponse.Serial
import com.mrtwon.framex_premium.GeneralVM
import com.mrtwon.framex_premium.room.SerialWithGenres
import com.mrtwon.framex_premium.room.Subscription
import kotlinx.coroutines.DelicateCoroutinesApi

class AboutSerialViewModel: ViewModel() {
    val old_model = MyApplication.getInstance.appComponents.getModel()
    val model = MyApplication.getInstance.appComponents.getNewModel()
    lateinit var isSubscriptionLiveData: LiveData<Subscription>
    val contentData = MutableLiveData<Serial?>()
    val isFavoriteBoolean = MutableLiveData<Boolean>()

    @DelicateCoroutinesApi
    fun getAbout(id: Int){
        model.getAboutSerial(id){
            contentData.postValue(it)
        }
    }
    fun initSubscriptionLiveData(id: Int): LiveData<Subscription>{
        return old_model.getSubscriptionByIdLiveData(id)
    }
    fun subscriptionAction(id: Int){
        old_model.subscriptionIf(id)
    }
    fun addFavorite(id: Int, contentType: String = "tv_series"){
        old_model.addFavorite(id, contentType)
    }
    fun deleteFavorite(id: Int, contentType: String = "tv_series"){
        old_model.removeFavorite(id, contentType)
    }
    fun isFavorite(id: Int, contentType: String = "tv_series"){
        old_model.isFavorite(id, contentType){
            isFavoriteBoolean.postValue(it)
        }
    }
}