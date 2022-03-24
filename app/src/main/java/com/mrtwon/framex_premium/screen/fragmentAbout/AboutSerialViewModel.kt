package com.mrtwon.framex_premium.screen.fragmentAbout

import androidx.lifecycle.*

import com.mrtwon.framex_premium.data.entity.FavoriteDao
import com.mrtwon.framex_premium.data.entity.SubscriptionDao
import com.mrtwon.framex_premium.data.localDataSource.liveData.LiveDataExisting
import com.mrtwon.framex_premium.domain.entity.Content
import com.mrtwon.framex_premium.domain.entity.enum.ContentEnum
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.usecase.*
import javax.inject.Inject


class AboutSerialViewModel(
    private val getContentById: GetContentById,
    private val existSubscriptionLiveData: ExistSubscriptionLiveData,
    private val existSubscription: ExistSubscription,
    private val existFavoriteLiveData: ExistFavoriteLiveData,
    private val existFavorite: ExistFavorite,
    private val addSubscription: AddSubscriptionContent,
    private val removeSubscription: RemoveSubscription,
    private val addFavoriteContent: AddFavoriteContent,
    private val removeFavoriteContent: RemoveFavoriteContent
): ViewModel() {
    private val contentEnum: ContentEnum = ContentEnum.Serial
    private val contentMutable = MutableLiveData<Content?>()
    private val favoriteExistMutable = MutableLiveData<LiveDataExisting<FavoriteDao?>>()
    private val subscriptionExistMutable = MutableLiveData<LiveDataExisting<SubscriptionDao?>>()
    private val errorMutable = MutableLiveData<Failure>()
    private val loadMutable = MutableLiveData<Boolean>()

    val favoriteExistLiveData: LiveData<LiveDataExisting<FavoriteDao?>> = favoriteExistMutable
    val subscriptionExistLiveData: LiveData<LiveDataExisting<SubscriptionDao?>> = subscriptionExistMutable
    val errorLiveData: LiveData<Failure> = errorMutable
    val contentLiveData: LiveData<Content?> = contentMutable
    val loadLiveData: LiveData<Boolean> = loadMutable


    fun actionFavorite(idContent: Int, contentEnum: ContentEnum){
        existFavorite(
            params = ExistFavorite.Param(contentId = idContent, contentEnum = contentEnum),
            scope = viewModelScope
        ){ either ->
            either.fold(
                { failure ->
                    errorMutable.postValue(failure)
                },
                { exist ->
                    when(exist){
                        true -> {
                            removeFavorite(idContent = idContent, contentEnum = contentEnum)
                        }
                        false -> {
                            addFavorite(idContent = idContent, contentEnum = contentEnum)
                        }
                    }
                }
            )
        }
    }
    fun actionSubscription(idContent: Int){
        existSubscription(
            params = ExistSubscription.Param(idContent),
            scope = viewModelScope
        ){ either ->
            either.fold(
                { failure ->
                    errorMutable.postValue(failure)
                },
                { exist ->
                    when(exist){
                        true -> {
                            removeSubscription(idContent)
                        }
                        false -> {
                            addSubscription(idContent)
                        }
                    }
                }
            )
        }
    }

    private fun removeFavorite(idContent: Int, contentEnum: ContentEnum){
        removeFavoriteContent(
            params = RemoveFavoriteContent.Param(id = idContent, contentEnum = contentEnum),
            scope = viewModelScope
        ){ either ->
            either.fold(
                { failure ->
                    errorMutable.postValue(failure)
                }, {}
            )
        }
    }
    private fun addFavorite(idContent: Int, contentEnum: ContentEnum){
        addFavoriteContent(
            params = AddFavoriteContent.Param(idContent, contentEnum),
            scope = viewModelScope
        ){ failure ->
            failure.fold(
                {
                    errorMutable.postValue(it)
                },{}
            )
        }
    }

    private fun addSubscription(idContent: Int){
        addSubscription(
            params = AddSubscriptionContent.Param(id = idContent),
            scope = viewModelScope
        ){ either ->
            either.fold(
                { failure ->
                    errorMutable.postValue(failure)
                }, {}
            )
        }
    }
    private fun removeSubscription(idContent: Int){
        removeSubscription(
            params = RemoveSubscription.Param(id = idContent),
            scope = viewModelScope
        ){ either ->
            either.fold(
                { failure ->
                    errorMutable.postValue(failure)
                },{}
            )
        }
    }

    private fun getFavoriteLiveData(idContent: Int){
        existFavoriteLiveData(
            params = ExistFavoriteLiveData.Param(idContent, contentEnum),
            scope = viewModelScope
        ){ either ->
            either.fold(
                { failure ->
                    errorMutable.postValue(failure)
                },
                { result ->
                    favoriteExistMutable.postValue(
                        result as LiveDataExisting<FavoriteDao?>
                    )
                }
            )
        }
    }
    private fun getSubscriptionLiveData(idContent: Int){
        existSubscriptionLiveData(
            params = ExistSubscriptionLiveData.Param(idContent),
            scope = viewModelScope
        ){ either ->
            either.fold(
                { failure ->
                    errorMutable.postValue(failure)
                },
                { result ->
                    subscriptionExistMutable.postValue(
                        result as LiveDataExisting<SubscriptionDao?>
                    )
                }
            )
        }
    }
    private fun getContent(idContent: Int){
        loadMutable.postValue(true)
        getContentById(
            params = GetContentById.Params(id = idContent, contentEnum),
            scope = viewModelScope
        ){ either ->
            loadMutable.postValue(false)
            either.fold(
                { failure ->
                    errorMutable.postValue(failure)
                },
                { result ->
                    contentMutable.postValue(result)
                }
            )
        }
    }
    fun initContent(idContent: Int){
        getContent(idContent = idContent)
        getSubscriptionLiveData(idContent = idContent)
        getFavoriteLiveData(idContent = idContent)
    }

    class Factory @Inject constructor(
        private val getContentById: GetContentById,
        private val existSubscriptionLiveData: ExistSubscriptionLiveData,
        private val existSubscription: ExistSubscription,
        private val existFavoriteLiveData: ExistFavoriteLiveData,
        private val existFavorite: ExistFavorite,
        private val addSubscription: AddSubscriptionContent,
        private val removeSubscription: RemoveSubscription,
        private val addFavoriteContent: AddFavoriteContent,
        private val removeFavoriteContent: RemoveFavoriteContent
    ): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return AboutSerialViewModel(
                getContentById = getContentById,
                existSubscriptionLiveData = existSubscriptionLiveData,
                existFavoriteLiveData = existFavoriteLiveData,
                existSubscription = existSubscription,
                existFavorite = existFavorite,
                addSubscription = addSubscription,
                removeSubscription = removeSubscription,
                addFavoriteContent = addFavoriteContent,
                removeFavoriteContent = removeFavoriteContent
            ) as T
        }

    }

}