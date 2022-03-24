package com.mrtwon.framex_premium.screen.fragmentTop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mrtwon.framex_premium.screen.fragmentTop.topCurrentYear.TopCurrentYearVM
import com.mrtwon.framex_premium.screen.fragmentTop.topGenres.TopGenresVM

class MainTopVM: ViewModel() {
    private val filterCurrentYearMutable = MutableLiveData<TopCurrentYearVM.Filter>(
        TopCurrentYearVM.Filter()
    )
    private val filterGenresMutable = MutableLiveData<TopGenresVM.Filter>(
        TopGenresVM.Filter()
    )

    val filterCurrentYearLD: LiveData<TopCurrentYearVM.Filter> = filterCurrentYearMutable
    val filterGenresLD: LiveData<TopGenresVM.Filter> = filterGenresMutable

    fun setFilterCurrentYear(filter: TopCurrentYearVM.Filter){
        filterCurrentYearMutable.postValue(filter)
    }
    fun setFilterGenres(filter: TopGenresVM.Filter){
        filterGenresMutable.postValue(filter)
    }
}