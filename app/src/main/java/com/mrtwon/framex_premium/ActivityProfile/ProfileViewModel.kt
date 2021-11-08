package com.mrtwon.framex_premium.ActivityProfile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mrtwon.framex_premium.GeneralVM
import com.mrtwon.framex_premium.Helper.DetailsError
import com.mrtwon.framex_premium.retrofit.framexAuth.ResponseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel: GeneralVM() {
    //state open dialog
    val isOpenEditNickName = mutableStateOf(false)
    val isOpenEditFavoriteContent = mutableStateOf(false)
    val isOpenEditAboutMe = mutableStateOf(false)
    val isOpenEditAccessFavorite = mutableStateOf(false)
    val isOpenHelpDialog = mutableStateOf(false)

    val isAuth = MutableLiveData<Boolean>()
    val profileLiveData = mutableStateOf<ResponseUser?>(null)
    val errorLiveData = mutableStateOf<DetailsError?>(null)
    val preErrorState = mutableStateOf<DetailsError?>(null)
    val preLoadState = mutableStateOf(false)
    val loadLiveData = mutableStateOf(false)
    val confirmEditLiveData = MutableLiveData<Boolean?>()
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    fun refresh(){
        viewModelScope.launch(Dispatchers.IO) {
            _isRefreshing.emit(true)
            loadLiveData.value = true
            model.giveMeUserProfile(
                {
                    _isRefreshing.tryEmit(false)
                    loadLiveData.value = false
                    errorLiveData.value = it
                },
                {
                    _isRefreshing.tryEmit(false)
                    loadLiveData.value = false
                    profileLiveData.value = it
                }
            )
        }
    }


    fun firstGiveProfile(){
        viewModelScope.launch(Dispatchers.IO) {
            preLoadState.value = true
            model.giveMeUserProfile(
                {
                    preLoadState.value = false
                    preErrorState.value = it
                },
                {
                    preLoadState.value = false
                    profileLiveData.value = it
                }
            )
        }
    }
    fun giveProfile(){
        viewModelScope.launch(Dispatchers.IO) {
            loadLiveData.value = true
            model.giveMeUserProfile(
                {
                    loadLiveData.value = false
                    errorLiveData.value = it
                },
                {
                    loadLiveData.value = false
                    profileLiveData.value = it
                }
            )
        }
    }

    fun giveAuthStatus(){
        model.isAuth(
            {
              isAuth.value = it
            },
            {
                errorLiveData.value = it
            }
        )
    }

    fun createNickName(nickName: String){
        viewModelScope.launch(Dispatchers.IO) {
            loadLiveData.value = true
            model.createNickName(nickName,
                {
                    loadLiveData.value = false
                    errorLiveData.value = it
                },
                {
                    loadLiveData.value = false
                    isOpenEditNickName.value = false
                    confirmEditLiveData.postValue(true)
                    giveProfile()
                })
        }
    }

    fun createAboutMe(about: String){
        viewModelScope.launch(Dispatchers.IO) {
            loadLiveData.value = true
            model.createAboutMe(about,
                {
                    loadLiveData.value = false
                    errorLiveData.value = it
                },
                {
                    loadLiveData.value = false
                    isOpenEditAboutMe.value = false
                    confirmEditLiveData.postValue(true)
                    giveProfile()
                })
        }
    }

    fun createFavorite(favorite: String){
        viewModelScope.launch(Dispatchers.IO) {
            loadLiveData.value = true
            model.createFavoriteContent(favorite,
                {
                    loadLiveData.value = false
                    errorLiveData.value = it
                },
                {
                    loadLiveData.value = false
                    isOpenEditFavoriteContent.value = false
                    confirmEditLiveData.postValue(true)
                    giveProfile()
                }
            )
        }
    }

    fun createOpenFavorite(isOpen: Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            loadLiveData.value = true
            model.createOpenFavorite(isOpen,
                {
                    loadLiveData.value = false
                    errorLiveData.value = it
                },
                {
                    loadLiveData.value = false
                    isOpenEditAccessFavorite.value = false
                    confirmEditLiveData.postValue(true)
                    giveProfile()
                }
            )
        }
    }

    fun logout(){
        model.logout { error ->
            errorLiveData.value = error
        }
    }
    override fun onCleared() {
        super.onCleared()
    }
}