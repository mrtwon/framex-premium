package com.mrtwon.framex_premium.screen.fragmentSearch.searchByDescription

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.mrtwon.framex_premium.domain.entity.ContentItemPage
import com.mrtwon.framex_premium.domain.entity.enum.RatingEnum
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.usecase.GetContentByDescriptionPaging
import com.mrtwon.testfirebase.paging.PagingCallback
import com.mrtwon.testfirebase.paging.pagingSearchByDescription.SearchByDescriptionPagedList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchByDescriptionVM(
    val getContentByDescriptionPaging: GetContentByDescriptionPaging
): ViewModel(), PagingCallback {
    private val firstLoadMutable = MutableLiveData<Boolean>()
    private val loadMutable = MutableLiveData<Boolean>()
    private val successfulMutable = MutableLiveData<Boolean>()
    private val notFoundMutable = MutableLiveData<Boolean>()
    private val errorMutable = MutableLiveData<Failure>()
    private val pagedListMutable = MutableLiveData<PagedList<ContentItemPage>>()
    private val filterMutable = MutableLiveData<Filter>()
    private val retryMutable = MutableLiveData<Boolean>()
    val firstLoadLD: LiveData<Boolean> = firstLoadMutable
    val loadLD: LiveData<Boolean> = loadMutable
    val successfulLD: LiveData<Boolean> = successfulMutable
    val notFoundLD: LiveData<Boolean> = notFoundMutable
    val errorLD: LiveData<Failure> = errorMutable
    val pagedListLD: LiveData<PagedList<ContentItemPage>> = pagedListMutable
    val filterLD: LiveData<Filter> = filterMutable

    fun retry(){
        retryMutable.value = true
        retryMutable.value = false
    }

    override fun onFirstLoad(state: Boolean) {
        viewModelScope.launch(Dispatchers.Main){
            firstLoadMutable.value = state
        }
    }
    override fun onLoad(state: Boolean) { viewModelScope.launch(Dispatchers.Main){
        loadMutable.value = state
        }
    }
    override fun onError(failure: Failure) { viewModelScope.launch(Dispatchers.Main){
        errorMutable.value = failure
        }
    }
    override fun onSuccessful(state: Boolean) {
        Log.i("self-search-desc", "successful vm $state")
        successfulMutable.value = state
    }
    override fun onNotFound(state: Boolean) { notFoundMutable.value = state }

    fun setFilter(filter: Filter){
        filterMutable.postValue(filter)
    }

    init {
        filterMutable.observeForever { filter ->
            pagedListMutable.postValue(
                SearchByDescriptionPagedList(
                    param = GetContentByDescriptionPaging.PrimaryParam(
                        descriptionList = filter.descriptionList,
                        sortBy = filter.sortBy
                    ),
                    useCase = getContentByDescriptionPaging,
                    coroutineScope = viewModelScope,
                    pagingCallback = this,
                    retryLD = retryMutable
                ).create()
            )
        }
    }


    data class Filter(var descriptionList: List<String>, val sortBy: RatingEnum = RatingEnum.Kinopoisk)

    class Factory @Inject constructor(
        val getContentByDescriptionPaging: GetContentByDescriptionPaging
    ): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SearchByDescriptionVM(getContentByDescriptionPaging) as T
        }

    }
}