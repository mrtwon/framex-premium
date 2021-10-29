package com.mrtwon.framex_premium.ActivityAuth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import com.mrtwon.framex_premium.GeneralVM
import com.mrtwon.framex_premium.Helper.DetailsError
import com.mrtwon.framex_premium.retrofit.framexAuth.ResponseUser

class CreateNickNameVM: GeneralVM() {
    val errorLiveData = mutableStateOf<DetailsError?>(value = null)
    val confirmLiveData = MutableLiveData<Boolean>()
    val profileLiveData = MutableLiveData<ResponseUser>()
    val loadLiveData = mutableStateOf(false)

    fun giveMeProfile(){
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

    fun createNickName(nickName: String){
        loadLiveData.value = true
        model.createNickName(nickName,
            {
                loadLiveData.value = false
                errorLiveData.value = it
            },
            {
                loadLiveData.value = false
                confirmLiveData.value = it
            }
        )
    }
    override fun onCleared() {
        super.onCleared()
    }
}