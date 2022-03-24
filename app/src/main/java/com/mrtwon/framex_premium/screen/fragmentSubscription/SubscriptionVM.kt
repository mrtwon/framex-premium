package com.mrtwon.framex_premium.screen.fragmentSubscription

import androidx.lifecycle.*
import com.mrtwon.framex_premium.data.localDataSource.liveData.LiveDataNotificationContent
import com.mrtwon.framex_premium.data.localDataSource.liveData.LiveDataSubscriptionContent
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.usecase.GetLiveDataNotification
import com.mrtwon.framex_premium.domain.usecase.GetLiveDataSubscription
import com.mrtwon.framex_premium.domain.usecase.RemoveNotificationContent
import com.mrtwon.framex_premium.domain.usecase.RemoveSubscription
import javax.inject.Inject

class SubscriptionVM constructor(
    private val getLiveDataSubscription: GetLiveDataSubscription,
    private val getLiveDataNotification: GetLiveDataNotification,
    private val removeSubscription: RemoveSubscription,
    private val removeNotificationContent: RemoveNotificationContent
): ViewModel() {
    init {
        getNotificationLiveData()
        getSubscriptionLiveData()
    }


    private val subscriptionMutable = MutableLiveData<LiveDataSubscriptionContent>()
    private val notificationMutable = MutableLiveData<LiveDataNotificationContent>()
    private val failureMutable = MutableLiveData<Failure>()
    val subscriptionLiveData: LiveData<LiveDataSubscriptionContent> = subscriptionMutable
    val notificationLiveData: LiveData<LiveDataNotificationContent> = notificationMutable
    val failureLiveData: LiveData<Failure> = failureMutable


    private fun getNotificationLiveData(){
        getLiveDataNotification(
            params = Unit,
            scope = viewModelScope
        ){ either ->
            either.fold(
                { failure ->
                    failureMutable.postValue(failure)
                },
                { result ->
                    notificationMutable.postValue(
                        result as LiveDataNotificationContent
                    )
                }
            )
        }
    }

    private fun getSubscriptionLiveData(){
        getLiveDataSubscription(
            params = Unit,
            scope = viewModelScope
        ){ either ->
            either.fold(
                { failure ->
                    failureMutable.postValue(failure)
                },
                { result ->
                    subscriptionMutable.postValue(
                        result as LiveDataSubscriptionContent
                    )
                }
            )
        }
    }

    fun removeSubscription(id: Int){
        removeSubscription(
            params = RemoveSubscription.Param(id),
            scope = viewModelScope
        ){ either ->
            either.fold(
                { failure ->
                    failureMutable.postValue(failure)
                },
                {}
            )
        }
    }

    fun removeNotification(id: Int) {
         removeNotificationContent(
             params = RemoveNotificationContent.Param(id),
             scope = viewModelScope
         ){ either ->
             either.fold(
                 {
                    failureMutable.postValue(it)
                 },
                 {}
             )
         }
    }

    class Factory @Inject constructor(
        private val getLiveDataSubscription: GetLiveDataSubscription,
        private val getLiveDataNotification: GetLiveDataNotification,
        private val removeSubscription: RemoveSubscription,
        private val removeNotificationContent: RemoveNotificationContent
    ): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SubscriptionVM(
                getLiveDataSubscription =  getLiveDataSubscription,
                getLiveDataNotification = getLiveDataNotification,
                removeSubscription = removeSubscription,
                removeNotificationContent = removeNotificationContent
            ) as T
        }

    }

}