package com.mrtwon.framex_premium.FragmentSubscription

import androidx.lifecycle.MutableLiveData
import com.mrtwon.framex_premium.GeneralVM
import com.mrtwon.framex_premium.room.Notification
import com.mrtwon.framex_premium.room.Subscription

class SubscriptionVM: GeneralVM() {
    val notificationListLiveData = model.getNotificationListLiveData()
    val subscriptionListLiveData = model.getSubscriptionListLiveData()


    fun addSubscription(subscription: Subscription){
        model.addSubscription(subscription)
    }
    fun removeSubscription(subscription: Subscription){
        model.removeSubscription(subscription)
    }

    fun removeNotification(notification: Notification){
        model.removeNotification(notification)
    }

}