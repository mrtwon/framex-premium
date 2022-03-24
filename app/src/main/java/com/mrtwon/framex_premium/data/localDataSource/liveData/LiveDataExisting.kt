package com.mrtwon.framex_premium.data.localDataSource.liveData

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.mrtwon.framex_premium.data.entity.FavoriteDao
import com.mrtwon.framex_premium.data.entity.SubscriptionDao
import com.mrtwon.framex_premium.domain.functional.SubscriptionInterface

class LiveDataExisting <T> (private val liveData: LiveData<T?>):
    SubscriptionInterface<Boolean> {
    private var lifecycleOwner: LifecycleOwner? = null
    private var foreverObserverList = arrayListOf<Observer<T?>>()

    override fun observe(observer: SubscriptionInterface.Observer<Boolean>) {
        if (lifecycleOwner != null) {
            liveData.observe(lifecycleOwner!!) {
                observer.onChange(it != null)
            }
        } else {
            val foreverObserver = Observer<T?> {
                observer.onChange(it != null)
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