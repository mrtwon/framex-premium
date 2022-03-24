package com.mrtwon.framex_premium.data.localDataSource.liveData

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.mrtwon.framex_premium.data.entity.SubscriptionDao
import com.mrtwon.framex_premium.domain.entity.Subscription
import com.mrtwon.framex_premium.domain.functional.SubscriptionInterface


class LiveDataSubscriptionContent(private val liveData: LiveData<List<SubscriptionDao>>):
    SubscriptionInterface<List<Subscription>> {
    private var lifecycleOwner: LifecycleOwner? = null
    private var foreverObserverList = arrayListOf<Observer<List<SubscriptionDao>>>()

    override fun observe(observer: SubscriptionInterface.Observer<List<Subscription>>) {
        if(lifecycleOwner != null){
            liveData.observe(lifecycleOwner!!){
                observer.onChange(arrayListOf<Subscription>().apply {
                    it.forEach{ oneSubscriptionElem ->
                        add(oneSubscriptionElem.toSubscription())
                    }
                })
            }
        }else{
            val foreverObserver = Observer<List<SubscriptionDao>> {
                observer.onChange(arrayListOf<Subscription>().apply {
                    it.forEach{ oneSubscriptionElem ->
                        add(oneSubscriptionElem.toSubscription())
                    }
                })
            }
            foreverObserverList.add(foreverObserver)
            liveData.observeForever(foreverObserver)
        }
    }

    override fun clear() {
        if (lifecycleOwner != null) {
            liveData.removeObservers(lifecycleOwner!!)
        } else {
            for (observe in foreverObserverList)
                liveData.removeObserver(observe)
        }
    }

    fun setLifecycleOwner(owner: LifecycleOwner){
        lifecycleOwner = owner
    }
}