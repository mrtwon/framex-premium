package com.mrtwon.framex_premium.FragmentSearch

import androidx.lifecycle.MutableLiveData
import com.mrtwon.framex_premium.ContentResponse.ContentResponse
import com.mrtwon.framex_premium.GeneralVM
import kotlinx.coroutines.DelicateCoroutinesApi

class SearchViewModel: GeneralVM() {
    val searchContent = MutableLiveData<List<ContentResponse>>()
    val searchQuery = MutableLiveData<String>()
    val searchQueryDescription = MutableLiveData<String>()
    val notFoundLiveData = MutableLiveData<Boolean>()
    val connectErrorLiveData = MutableLiveData<Boolean>()
    val loadLiveData = MutableLiveData<Boolean>()

    init {
        searchQuery.observeForever{
            search(it)
        }
        searchQueryDescription.observeForever{
            searchDescription(it)
        }
    }


    private fun search(searchString: String){
        loadLiveData.postValue(true)
        model.searchContentByTitle(searchString,
            {
                loadLiveData.postValue(false)
                searchContent.postValue(it)
            },
            {
                loadLiveData.postValue(false)
                connectErrorLiveData.postValue(true)
            }
        )
    }
    fun searchDescription(searchString: String){
        loadLiveData.postValue(true)
        model.searchContentByDescription(searchString,
        {
            loadLiveData.postValue(false)
            searchContent.postValue(it)
        },
        {
            loadLiveData.postValue(false)
            connectErrorLiveData.postValue(true)
        }
        )
    }
}