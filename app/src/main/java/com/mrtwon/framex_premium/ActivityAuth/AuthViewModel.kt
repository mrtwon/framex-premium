package com.mrtwon.framex_premium.ActivityAuth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mrtwon.framex_premium.GeneralVM
import com.mrtwon.framex_premium.Helper.DetailsError

class AuthViewModel: GeneralVM() {
    val loadLiveData = mutableStateOf(false)
    val confirmLiveData = MutableLiveData(false)
    val errorLiveData = mutableStateOf<DetailsError?>(null)

    fun loginWithEmailPassword(email: String, password: String){
        loadLiveData.value = true
        model.login(email = email, password = password,
            {
                loadLiveData.value = false
            errorLiveData.value = it
            },
            {
                loadLiveData.value = false
                confirmLiveData.value = it
            })
    }

    fun resetPassword(email: String){

    }

    override fun onCleared() {
        super.onCleared()
    }
}