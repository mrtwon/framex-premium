package com.mrtwon.framex_premium.screen.fragmentTop.topCurrentYear

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.PagedList
import com.mrtwon.framex_premium.domain.entity.ContentItemPage
import com.mrtwon.framex_premium.domain.entity.enum.ContentEnum
import com.mrtwon.framex_premium.domain.entity.enum.GenresEnum
import com.mrtwon.framex_premium.domain.entity.enum.RatingEnum
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.usecase.GetTopContentByYearPaging
import com.mrtwon.testfirebase.paging.PagingCallback
import com.mrtwon.testfirebase.paging.pagingTopOfCurrentYear.TopOfCurrentYearPagedList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer

class TopCurrentYearVM constructor(
    private val getTopContentByYearPaging: GetTopContentByYearPaging
): ViewModel(), PagingCallback {
    private val primaryFilterMutable = MutableLiveData<PrimaryFilter>()
    private val firstLoadMutable = MutableLiveData<Boolean>()
    private val loadMutable = MutableLiveData<Boolean>()
    private val errorMutable = MutableLiveData<Failure>()
    private val pagedListMutable = MutableLiveData<PagedList<ContentItemPage>?>()
    private val notFoundMutable = MutableLiveData<Boolean>()
    private val successfulMutable = MutableLiveData<Boolean>()
    private var primaryFilter: PrimaryFilter? = null
    private val retryMutable = MutableLiveData<Boolean>()
    val pagedListLiveData: LiveData<PagedList<ContentItemPage>?> = pagedListMutable
    val errorLiveData: LiveData<Failure> = errorMutable
    val loadLiveData: LiveData<Boolean> = loadMutable
    val notFoundLiveData: LiveData<Boolean> = notFoundMutable
    val successfulLiveData: LiveData<Boolean> = successfulMutable
    val firstLoadLiveData: LiveData<Boolean> = firstLoadMutable

    fun setPrimaryFilter(primaryFilter: PrimaryFilter){
        this.primaryFilter = primaryFilter
    }

    fun changeFilter(filter: Filter){
        primaryFilter?.let { primaryFilterNotNull ->
            pagedListMutable.value =
                TopOfCurrentYearPagedList(
                    param = GetTopContentByYearPaging.PrimaryParams(
                        sortBy = filter.sortBy,
                        content = primaryFilterNotNull.content,
                        year = primaryFilterNotNull.year,
                        genresEnum = filter.genres
                    ),
                    useCase = getTopContentByYearPaging,
                    coroutineScope = viewModelScope,
                    callback = this,
                    retryLiveData = retryMutable
                ).create()
        }
        pagedListMutable.value = null
    }

    data class PrimaryFilter(
        var content: ContentEnum,
        var year: Int
    )
    data class Filter(
        var sortBy: RatingEnum = RatingEnum.Kinopoisk,
        var genres: GenresEnum = GenresEnum.None
    )

    class Factory @Inject constructor(
        val getTopContentByYearPaging: GetTopContentByYearPaging
    ): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return TopCurrentYearVM(getTopContentByYearPaging) as T
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