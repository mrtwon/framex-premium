package com.mrtwon.framex_premium.ActivityAuth

import androidx.compose.material.contentColorFor
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mrtwon.framex_premium.GeneralVM
import com.mrtwon.framex_premium.Helper.DetailsError
import com.mrtwon.framex_premium.retrofit.framexAuth.ResponseError

class RegistrationViewModel: GeneralVM() {
    val errorLiveData = mutableStateOf<DetailsError?>(value = null)
    val confirmLiveData = MutableLiveData<Boolean>()
    val loadLiveData = mutableStateOf(false)

    fun sendCreateUserRequest(email: String, password: String){
        loadLiveData.value = true
        model.sendCreateUserRequest(
            email, password,
            {
                loadLiveData.value = false
                errorLiveData.value = it
            },{
                loadLiveData.value = false
                confirmLiveData.postValue(it)
            })
    }
    override fun onCleared() {
        super.onCleared()
    }
}