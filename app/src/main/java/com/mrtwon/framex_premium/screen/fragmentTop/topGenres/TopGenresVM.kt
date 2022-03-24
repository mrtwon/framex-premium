package com.mrtwon.framex_premium.screen.fragmentTop.topGenres

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.mrtwon.framex_premium.domain.entity.ContentItemPage
import com.mrtwon.framex_premium.domain.entity.enum.ContentEnum
import com.mrtwon.framex_premium.domain.entity.enum.GenresEnum
import com.mrtwon.framex_premium.domain.entity.enum.RatingEnum
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.usecase.GetTopContentByGenresPaging
import com.mrtwon.testfirebase.paging.PagingCallback
import com.mrtwon.framex_premium.paging.pagingTopByGenres.TopByGenresPagedList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class TopGenresVM constructor(
    private val getTopContentByGenresPaging: GetTopContentByGenresPaging
): ViewModel(), PagingCallback {
    private val loadMutable = MutableLiveData<Boolean>()
    private val errorMutable = MutableLiveData<Failure>()
    private val pagedListMutable = MutableLiveData<PagedList<ContentItemPage>>()
    private val notFoundMutable = MutableLiveData<Boolean>()
    private val successfulMutable = MutableLiveData<Boolean>()
    private val firstLoadMutable = MutableLiveData<Boolean>()
    private var primaryFilter: TopGenresVM.PrimaryFilter? = null
    private val retryMutable = MutableLiveData<Boolean>()

    val pagedListLiveData: LiveData<PagedList<ContentItemPage>> = pagedListMutable
    val errorLiveData: LiveData<Failure> = errorMutable
    val loadLiveData: LiveData<Boolean> = loadMutable
    val notFoundLiveData: LiveData<Boolean> = notFoundMutable
    val successfulLiveData: LiveData<Boolean> = successfulMutable
    val firstLoadLiveData: LiveData<Boolean> = firstLoadMutable


    fun setPrimaryFilter(primaryFilter: PrimaryFilter){
        this.primaryFilter = primaryFilter
    }
    fun changeFilter(changedFilter: TopGenresVM.Filter){
        primaryFilter?.let { primaryFilterNotNull ->
            pagedListMutable.postValue(
                TopByGenresPagedList(
                    param = GetTopContentByGenresPaging.PrimaryParams(
                        content = primaryFilterNotNull.content,
                        sortBy = changedFilter.sortBy,
                        genres = primaryFilterNotNull.genres
                    ),
                    useCase = getTopContentByGenresPaging,
                    coroutineScope = viewModelScope,
                    callback = this,
                    retryLD = retryMutable
                ).create()
            )
        }
    }
    private fun setError(failure: Failure){
        errorMutable.postValue(failure)
    }


    data class Filter(
        var sortBy: RatingEnum = RatingEnum.Kinopoisk
    )
    data class PrimaryFilter(
        var genres: GenresEnum,
        var content: ContentEnum
    )

    class Factory @Inject constructor(
        val getTopContentByGenresPaging: GetTopContentByGenresPaging
    ): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return TopGenresVM(getTopContentByGenresPaging) as T
        }

    }

    fun retry(){
        retryMutable.value = true
        retryMutable.value = false
    }

    override fun onFirstLoad(state: Boolean) {
        viewModelScope.launch(Dispatchers.Main) {
            firstLoadMutable.value = state
        }
    }

    override fun onLoad(state: Boolean) {
        viewModelScope.launch(Dispatchers.Main){
            loadMutable.value = state
        }
    }

    override fun onError(failure: Failure) {
        viewModelScope.launch(Dispatchers.Main){
            errorMutable.value = failure
        }
    }

    override fun onSuccessful(state: Boolean) {
        viewModelScope.launch(Dispatchers.Main){
            successfulMutable.value = state
        }
    }

    override fun onNotFound(state: Boolean) {
        viewModelScope.launch(Dispatchers.Main){
            notFoundMutable.value = state
        }
    }
}