package com.mrtwon.framex_premium.ActivityAuth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mrtwon.framex_premium.GeneralVM
import com.mrtwon.framex_premium.Helper.DetailsError
import com.mrtwon.framex_premium.retrofit.framexAuth.ResponseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateNickNameVM: GeneralVM() {
    val errorLiveData = mutableStateOf<DetailsError?>(value = null)
    val confirmLiveData = MutableLiveData<Boolean>()
    val profileLiveData = MutableLiveData<ResponseUser>()
    val loadLiveData = mutableStateOf(false)

    fun giveMeProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            loadLiveData.value = true
            model.giveMeUserProfile(
                {
                    loadLiveData.value = false
                    errorLiveData.value = it
                },
                {
                    loadLiveData.value = false
                    profileLiveData.postValue(it)
                }
            )
        }
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
                    confirmLiveData.postValue(it)
                }
            )
        }
    }
    override fun onCleared() {
        super.onCleared()
    }
}