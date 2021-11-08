package com.mrtwon.framex_premium.ActivityProfile

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrtwon.framex_premium.GeneralVM
import com.mrtwon.framex_premium.Helper.DetailsError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdatePhotoVM: GeneralVM() {
    val uriState = mutableStateOf<Uri?>(null)

    val loadPhotoState = mutableStateOf(false)
    val loadState = mutableStateOf(false)

    val preLoadState = mutableStateOf<Boolean>(false)
    val preErrorState = mutableStateOf<DetailsError?>(null)

    val confirmLiveData = MutableLiveData<Boolean>()
    val errorState = mutableStateOf<DetailsError?>(null)

    fun createPhoto(photoUri: Uri){
        viewModelScope.launch(Dispatchers.IO) {
            loadState.value = true
            model.createPhoto(photoUri,
                {
                    loadState.value = false
                    errorState.value = it
                },
                {
                    loadState.value = false
                    confirmLiveData.postValue(it)
                })
        }
    }

    fun giveMyPhoto(){
        viewModelScope.launch(Dispatchers.IO) {
            preLoadState.value = true
            model.giveMeUserProfile(
                {
                    preLoadState.value = false
                    preErrorState.value = it
                },
                {
                    preLoadState.value = false
                    uriState.value = Uri.parse(it.response[0].photo)
                }
            )
        }

    }
}