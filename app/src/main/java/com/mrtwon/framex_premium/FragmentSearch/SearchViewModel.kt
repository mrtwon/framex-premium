package com.mrtwon.framex_premium.FragmentSearch

import androidx.lifecycle.MutableLiveData
import com.mrtwon.framex_premium.ContentResponse.ContentResponse
import com.mrtwon.framex_premium.GeneralVM
import kotlinx.coroutines.DelicateCoroutinesApi

class SearchViewModel: GeneralVM() {
    val searchContent = MutableLiveData<List<ContentResponse>>()

    @DelicateCoroutinesApi
    fun search(searchString: String){
        model.searchContentByTitle(searchString){
           searchContent.postValue(it)
        }
    }
    @DelicateCoroutinesApi
    fun searchDescription(searchString: String){
        model.searchContentByDescription(searchString){
            searchContent.postValue(it)
        }
    }
}