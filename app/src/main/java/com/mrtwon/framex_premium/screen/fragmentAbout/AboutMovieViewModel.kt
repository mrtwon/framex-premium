package com.mrtwon.framex_premium.screen.fragmentAbout

import android.util.Log
import androidx.lifecycle.*

import com.mrtwon.framex_premium.data.entity.FavoriteDao
import com.mrtwon.framex_premium.data.entity.SubscriptionDao
import com.mrtwon.framex_premium.data.localDataSource.liveData.LiveDataExisting
import com.mrtwon.framex_premium.domain.entity.Content
import com.mrtwon.framex_premium.domain.entity.enum.ContentEnum
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.usecase.*
import javax.inject.Inject


class AboutMovieViewModel(
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
        Log.i("self-favorite","action favorite click")
        existFavorite(
            params = ExistFavorite.Param(contentId = idContent, contentEnum = contentEnum),
            scope = viewModelScope
        ){ either ->
            either.fold(
                { failure ->
                    Log.i("self-favorite","error get state existing")
                    errorMutable.postValue(failure)
                },
                { exist ->
                    when(exist){
                        true -> {
                            Log.i("self-favorite","exist favorite")
                            removeFavorite(idContent = idContent, contentEnum = contentEnum)
                        }
                        false -> {
                            Log.i("self-favorite","not exist favorite")
                            addFavorite(idContent = idContent, contentEnum = contentEnum)
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
                   Log.i("self-favorite","error add favorite")
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
                    Log.i("self-favorite","error add favorite")
                    errorMutable.postValue(it)
                },{}
            )
        }
    }


    private fun getFavoriteLiveData(idContent: Int){
        existFavoriteLiveData(
            params = ExistFavoriteLiveData.Param(idContent, ContentEnum.Movie),
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
            params = GetContentById.Params(id = idContent, ContentEnum.Movie),
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
            return AboutMovieViewModel(
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



    /*val contentData = MutableLiveData<Movie?>()
    val notFoundLiveData = MutableLiveData<Boolean>()
    val connectErrorLiveData = MutableLiveData<Boolean>()
    val loadLiveData = MutableLiveData<Boolean>()
    val loadState = mutableStateOf(false)
    val errorState = mutableStateOf<DetailsError?>(null)
    val confirmLiveData = MutableLiveData<Boolean>()*/

    /*@DelicateCoroutinesApi
    fun getAbout(id: Int){
        loadLiveData.postValue(true)
        model.getAboutMovie(id,
            {
                loadLiveData.postValue(false)
                contentData.postValue(it)
            },
            {
                loadLiveData.postValue(false)
                connectErrorLiveData.postValue(it)
            }
        )
    }

    fun getFavoriteLiveData(id: Int): LiveData<Favorite>{
        return model.db.dao().getFavoriteIdLiveData(id)
    }

    fun favoriteAction(contentResponse: ContentResponse){
        viewModelScope.launch {
            loadState.value = true
            model.favoriteAction(contentResponse,
            {
                loadState.value = false
                errorState.value = it
            },
             {
                 loadState.value = false
                 confirmLiveData.postValue(it)
             }
            )
        }
    }*/
}