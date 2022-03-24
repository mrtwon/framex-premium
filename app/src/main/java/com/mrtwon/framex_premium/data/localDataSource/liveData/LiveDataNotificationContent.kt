package com.mrtwon.framex_premium.data.localDataSource.liveData

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.mrtwon.framex_premium.data.entity.NotificationDao
import com.mrtwon.framex_premium.domain.entity.Notification
import com.mrtwon.framex_premium.domain.functional.SubscriptionInterface

class LiveDataNotificationContent(private val liveData: LiveData<List<NotificationDao>>):
    SubscriptionInterface<List<Notification>> {
    private var lifecycleOwner: LifecycleOwner? = null
    private var foreverObserverList = arrayListOf<Observer<List<NotificationDao>>>()

    override fun observe(observer: SubscriptionInterface.Observer<List<Notification>>) {
        if(lifecycleOwner != null){
            liveData.observe(lifecycleOwner!!){ daoList ->
                observer.onChange(arrayListOf<Notification>().apply {
                    daoList.forEach { oneDaoElement ->
                        this.add(oneDaoElement.toNotification())
                    }
                })
            }
        }else{
            val foreverObserver = Observer<List<NotificationDao>> { daoList ->
                observer.onChange(arrayListOf<Notification>().apply {
                    daoList.forEach { oneDaoElement ->
                        this.add(oneDaoElement.toNotification())
                    }
                })
            }
            foreverObserverList.add(foreverObserver)
            liveData.observeForever(foreverObserver)
        }
    }

    fun setLifecycleOwner(owner: LifecycleOwner){
        lifecycleOwner = owner
    }


    override fun clear() {
        if (lifecycleOwner != null) {
            liveData.removeObservers(lifecycleOwner!!)
        } else {
            for (observe in foreverObserverList)
                liveData.removeObserver(observe)
        }
    }
}