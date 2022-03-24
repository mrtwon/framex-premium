package com.mrtwon.framex_premium.screen.mainActivity

import androidx.lifecycle.*
import com.mrtwon.framex_premium.data.localDataSource.liveData.LiveDataRecentContent
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.usecase.GetLiveDataRecent
import javax.inject.Inject

class MainViewModel constructor(private val getLiveDataRecent: GetLiveDataRecent): ViewModel() {
    init {
        getRecentLiveData()
    }


    private val failureMutable = MutableLiveData<Failure>()
    private val recentMutable = MutableLiveData<LiveDataRecentContent>()
    val recentLiveData: LiveData<LiveDataRecentContent> = recentMutable
    val failureLiveData: LiveData<Failure> = failureMutable
    private fun getRecentLiveData(){
        getLiveDataRecent(params = Unit, scope = viewModelScope){ either ->
            either.fold(
                { failure ->
                    failureMutable.postValue(failure)
                },
                { result ->
                    recentMutable.postValue(
                        result as LiveDataRecentContent
                    )
                }
            )
        }
    }

    /*fun cleanHistory(){
        viewModelScope.launch(Dispatchers.IO){
            model.cleanHistory()
        }
    }*/

    class Factory @Inject constructor(
        val useCase: GetLiveDataRecent
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(useCase) as T
        }
    }
}