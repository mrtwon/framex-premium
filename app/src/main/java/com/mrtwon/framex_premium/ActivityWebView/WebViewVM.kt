package com.mrtwon.framex_premium.ActivityWebView

import androidx.lifecycle.MutableLiveData
import com.mrtwon.framex_premium.ContentResponse.ContentResponse
import com.mrtwon.framex_premium.GeneralVM
import com.mrtwon.framex_premium.room.Content

class WebViewVM: GeneralVM() {
    val content = MutableLiveData<ContentResponse?>()

    fun getVideoLink(id: Int, contentType: String){
        model.getContentResponse(id, contentType){ contentResponse ->
            content.postValue(contentResponse)
            model.addRecent(contentResponse)
        }
    }
}