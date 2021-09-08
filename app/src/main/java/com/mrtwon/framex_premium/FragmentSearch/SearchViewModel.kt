package com.mrtwon.framex_premium.FragmentSearch

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.mrtwon.framex_premium.ContentResponse.ContentResponse
import com.mrtwon.framex_premium.GeneralVM
import kotlinx.coroutines.DelicateCoroutinesApi

class SearchViewModel: GeneralVM() {
    val searchContent = MutableLiveData<List<ContentResponse>?>()
    private val searchContentSerial = MutableLiveData<List<ContentResponse>>()
    private val searchContentMovie = MutableLiveData<List<ContentResponse>>()
    val searchQuery = MutableLiveData<String>()
    val searchQueryDescription = MutableLiveData<String>()
    val notFoundLiveData = MutableLiveData<Boolean>()
    val connectErrorLiveData = MutableLiveData<Boolean>()
    val loadLiveData = MutableLiveData<Boolean>()

    var currentPageSerial = 1
    var currentPageMovie = 1
    var lastPageSerial = 0
    var lastPageMovie = 0

    init {
        searchQuery.observeForever {
            searchTitle(it)
        }
        searchQueryDescription.observeForever {
            searchDescription(it)
        }

    }


    private fun searchTitle(searchString: String, pageMovie: Int? = 1, pageSerial: Int? = 1) {
        loadLiveData.postValue(true)
        model.searchContentByTitle(searchString, pageMovie, pageSerial,
            {
                lastPageSerial = if (it.isNotEmpty()) it[it.lastIndex].last_page else 0
            },
            {
                lastPageMovie = if (it.isNotEmpty()) it[it.lastIndex].last_page else 0
            },
            {
                currentPageMovie = 1
                currentPageSerial = 1
                loadLiveData.postValue(false)
                searchContent.postValue(it)
            },
            {
                loadLiveData.postValue(false)
                connectErrorLiveData.postValue(it)
            })
    }

    fun searchDescription(searchString: String, pageMovie: Int? = 1, pageSerial: Int? = 1) {
        loadLiveData.postValue(true)
        model.searchContentByDescription(searchString, pageMovie, pageSerial,
            {
                lastPageMovie = if (it.isNotEmpty()) it[it.lastIndex].last_page else 0
                //log("input movie last page ${it[it.lastIndex].last_page}")
            },
            {
                //log("input serial last page ${it[it.lastIndex].last_page}")
                lastPageSerial = if (it.isNotEmpty()) it[it.lastIndex].last_page else 0
            },
            {
                currentPageSerial = 1
                currentPageMovie = 1
                //log("new content, size = ${it.size}")
                loadLiveData.postValue(false)
                searchContent.postValue(it)
            },
            {
                loadLiveData.postValue(false)
                connectErrorLiveData.postValue(it)
            })
    }

    fun nextPageDescription(searchString: String? = searchQueryDescription.value) {
        //log("nextPage Description, lastMovie ${lastPageMovie}[${currentPageMovie}], lastSerial ${lastPageSerial}[${currentPageSerial}]")
        var nextPageMovie: Int? = null
        var nextPageSerial: Int? = null
        if (lastPageMovie > currentPageMovie) {
            currentPageMovie++
            nextPageMovie = currentPageMovie
        }
        if (lastPageSerial > currentPageSerial) {
            currentPageSerial++
            nextPageSerial = currentPageSerial
        }
        log("Movie(last $lastPageMovie, current ${currentPageMovie}), Serial(last ${lastPageSerial}, current ${currentPageSerial})")
        if(nextPageMovie == null && nextPageSerial == null) return
        if (searchString != null) {
            loadLiveData.postValue(true)
            model.searchContentByDescription(searchString, nextPageMovie, nextPageSerial,
                {
                    lastPageMovie = if (it.isNotEmpty()) it[it.lastIndex].last_page else 0
                    //log("input movie last page ${it[it.lastIndex].last_page}")
                },
                {
                    //log("input serial last page ${it[it.lastIndex].last_page}")
                    lastPageSerial = if (it.isNotEmpty()) it[it.lastIndex].last_page else 0
                },
                {
                    //log("new content, size = ${it.size}")
                    loadLiveData.postValue(false)
                    searchContent.postValue(it)
                },
                {
                    loadLiveData.postValue(false)
                    connectErrorLiveData.postValue(it)
                })
        }
    }
    fun nextPageTitle(searchString: String? = searchQuery.value) {
        // log("nextPage Description, lastMovie ${lastPageMovie}[${currentPageMovie}], lastSerial ${lastPageSerial}[${currentPageSerial}]")
        var nextPageMovie: Int? = null
        var nextPageSerial: Int? = null
        if (lastPageMovie > currentPageMovie) {
            currentPageMovie++
            nextPageMovie = currentPageMovie
        }
        if (lastPageSerial > currentPageSerial) {
            currentPageSerial++
            nextPageSerial = currentPageSerial
        }
        if (nextPageMovie == null && nextPageSerial == null) return
        if (searchString != null) {
            loadLiveData.postValue(true)
            model.searchContentByTitle(searchString, nextPageMovie, nextPageSerial,
                {
                    //log("input serial last page ${it[it.lastIndex].last_page}")
                    lastPageSerial = if (it.isNotEmpty()) it[it.lastIndex].last_page else 0
                },
                {
                    lastPageMovie = if (it.isNotEmpty()) it[it.lastIndex].last_page else 0
                    //log("input movie last page ${it[it.lastIndex].last_page}")
                },
                {
                    loadLiveData.postValue(false)
                    //log("new content, size = ${it.size}")
                    searchContent.postValue(it)
                },
                {
                    loadLiveData.postValue(false)
                    connectErrorLiveData.postValue(it)
                })
        }
    }

    override fun onCleared() {
        log("onCleared()")
        super.onCleared()
    }

    fun log(s: String){
        Log.i("self-search-vm",s)
    }
}