package com.mrtwon.framex_premium.screen.fragmentSearch.searchByTitle

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.mrtwon.framex_premium.domain.entity.ContentItemPage
import com.mrtwon.framex_premium.domain.entity.enum.RatingEnum
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.usecase.GetContentByTitlePaging
import com.mrtwon.testfirebase.paging.PagingCallback
import com.mrtwon.testfirebase.paging.pagingSearchByTitle.SearchByTitlePagedList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchByTitleVM(
    private val getContentByTitlePaging: GetContentByTitlePaging
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

    override fun onFirstLoad(state: Boolean) {
        viewModelScope.launch(Dispatchers.Main) {
            firstLoadMutable.postValue(state)
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
    override fun onSuccessful(state: Boolean) { viewModelScope.launch(Dispatchers.Main){
        successfulMutable.value = state
        }
    }
    override fun onNotFound(state: Boolean) { viewModelScope.launch(Dispatchers.Main){
        notFoundMutable.value = state
        }
    }

    fun setFilter(filter: Filter){
        filterMutable.value = filter
    }
    fun retry(){
        retryMutable.value = true
        retryMutable.value = false
    }

    init {
        filterMutable.observeForever { filter ->
            pagedListMutable.postValue(
                SearchByTitlePagedList(
                    param = GetContentByTitlePaging.PrimaryParam(
                        title = filter.title,
                        sortBy = filter.sortBy
                    ),
                    useCase = getContentByTitlePaging,
                    coroutineScope = viewModelScope,
                    callback = this,
                    retryLD = retryMutable
                ).create()
            )
        }
    }


    data class Filter(var title: String, val sortBy: RatingEnum = RatingEnum.Kinopoisk)

    class Factory @Inject constructor(
        val getContentByTitlePaging: GetContentByTitlePaging
    ): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SearchByTitleVM(getContentByTitlePaging) as T
        }

    }
}