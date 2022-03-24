package com.mrtwon.framex_premium.data.localDataSource.liveData

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.mrtwon.framex_premium.data.entity.RecentDao
import com.mrtwon.framex_premium.domain.entity.Recent
import com.mrtwon.framex_premium.domain.functional.SubscriptionInterface

class LiveDataRecentContent(private val liveData: LiveData<List<RecentDao>>):
    SubscriptionInterface<List<Recent>> {
    private var lifecycleOwner: LifecycleOwner? = null
    private var foreverObserverList = arrayListOf<Observer<List<RecentDao>>>()

    override fun observe(observer: SubscriptionInterface.Observer<List<Recent>>) {
        if(lifecycleOwner != null){
            liveData.observe(lifecycleOwner!!){
                observer.onChange(arrayListOf<Recent>().apply {
                    it.forEach{ oneRecentDao ->
                        add(oneRecentDao.toRecent())
                    }
                })
            }
        }else{
            val foreverObserver = Observer<List<RecentDao>> {
                observer.onChange(arrayListOf<Recent>().apply {
                    it.forEach{ oneRecentDao ->
                        add(oneRecentDao.toRecent())
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

    override fun clear(){
        if (lifecycleOwner != null) {
            liveData.removeObservers(lifecycleOwner!!)
        } else {
            for (observe in foreverObserverList)
                liveData.removeObserver(observe)
        }
    }
}