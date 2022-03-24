package com.mrtwon.framex_premium.data.localDataSource.liveData

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.mrtwon.framex_premium.data.entity.FavoriteDao
import com.mrtwon.framex_premium.domain.entity.Favorite
import com.mrtwon.framex_premium.domain.functional.SubscriptionInterface

class LiveDataFavoriteContent constructor(private val liveData: LiveData<List<FavoriteDao>>):
    SubscriptionInterface<List<Favorite>> {
    private var lifecycleOwner: LifecycleOwner? = null
    private var foreverObserverList = arrayListOf<Observer<List<FavoriteDao>>>()
    override fun observe(observer: SubscriptionInterface.Observer<List<Favorite>>) {
        if(lifecycleOwner != null){
            liveData.observe(lifecycleOwner!!){
                observer.onChange(arrayListOf<Favorite>().apply {
                    it.forEach{ oneFavoriteDao ->
                        add(oneFavoriteDao.toFavorite())
                    }
                })
            }
        }else{
            val foreverObserver = Observer<List<FavoriteDao>> {
                observer.onChange(arrayListOf<Favorite>().apply {
                    it.forEach{ oneFavoriteDao ->
                        add(oneFavoriteDao.toFavorite())
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