package com.mrtwon.framex_premium.FragmentSubscription

import androidx.lifecycle.MutableLiveData
import com.mrtwon.framex_premium.GeneralVM
import com.mrtwon.framex_premium.room.Notification
import com.mrtwon.framex_premium.room.Serial

class SubscriptionVM: GeneralVM() {
    val notificationListLiveData = model.getNotificationListLiveData()
    private val subscriptionForActionLiveData = model.getSubscriptionListLiveData()
    val subscriptionListLiveData = MutableLiveData<List<Serial>>()

    init {
        subscriptionForActionLiveData.observeForever {
            getSubscription()
        }
    }

    private fun getSubscription(){
        model.getSubscriptionList {
            subscriptionListLiveData.postValue(it)
        }
    }

    fun removeSubscription(id: Int){
        model.removeSubscription(id)
    }

    fun removeNotification(notification: Notification){
        model.removeNotification(notification)
    }

}