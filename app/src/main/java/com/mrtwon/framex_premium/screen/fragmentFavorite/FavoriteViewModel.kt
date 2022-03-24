package com.mrtwon.framex_premium.screen.fragmentFavorite

import androidx.lifecycle.*
import com.mrtwon.framex_premium.data.localDataSource.liveData.LiveDataFavoriteContent
import com.mrtwon.framex_premium.domain.entity.enum.ContentEnum
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.usecase.GetLiveDataFavorite
import com.mrtwon.framex_premium.domain.usecase.RemoveFavoriteContent
import javax.inject.Inject

class FavoriteViewModel constructor(
    private val getLiveDataFavorite: GetLiveDataFavorite,
    private val removeFavoriteContent: RemoveFavoriteContent
): ViewModel() {
    init {
        getFavoriteLiveData()
    }

    private val favoriteMutable = MutableLiveData<LiveDataFavoriteContent>()
    private val failureMutable = MutableLiveData<Failure>()
    val favoriteLiveData: LiveData<LiveDataFavoriteContent> = favoriteMutable
    val failureLiveData: LiveData<Failure> = failureMutable

    fun removeFavorite(id: Int, contentEnum: ContentEnum){
        removeFavoriteContent(
            RemoveFavoriteContent.Param(id = id, contentEnum = contentEnum),
            viewModelScope
        ){ either ->
            either.fold(
                { failure ->
                   failureMutable.postValue(failure)
                }, {}
            )
        }
    }

    private fun getFavoriteLiveData(){
        getLiveDataFavorite(
            params = Unit,
            scope = viewModelScope
        ){ either ->
            either.fold(
                { failure ->
                    failureMutable.postValue(failure)
                },
                { result ->
                    favoriteMutable.postValue(result as LiveDataFavoriteContent)
                }
            )
        }
    }

    class Factory @Inject constructor(
        private val getLiveDataFavorite: GetLiveDataFavorite,
        private val removeFavoriteContent: RemoveFavoriteContent
    ): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return FavoriteViewModel(
                getLiveDataFavorite = getLiveDataFavorite,
                removeFavoriteContent = removeFavoriteContent
            ) as T
        }

    }

}







