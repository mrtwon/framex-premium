package com.mrtwon.framex_premium.screen.activityWebView

import android.webkit.WebView
import androidx.lifecycle.*
import com.mrtwon.framex_premium.Helper.HelperFunction
import com.mrtwon.framex_premium.domain.entity.Content
import com.mrtwon.framex_premium.domain.entity.Recent
import com.mrtwon.framex_premium.domain.entity.enum.ContentEnum
import com.mrtwon.framex_premium.domain.exception.Failure
import com.mrtwon.framex_premium.domain.usecase.AddRecentContent
import com.mrtwon.framex_premium.domain.usecase.GetContentById
import com.mrtwon.framex_premium.domain.usecase.GetRecentById
import com.mrtwon.framex_premium.domain.usecase.UpdateRecentContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class WebViewVM constructor(
    val getContentById: GetContentById,
    val getRecentById: GetRecentById,
    val addRecentContent: AddRecentContent,
    val updateRecentContent: UpdateRecentContent

): ViewModel() {
    private val contentMutable = MutableLiveData<Content>()
    private val errorMutable = MutableLiveData<Failure>()
    val contentLD: LiveData<Content> = contentMutable
    val errorLD: LiveData<Failure> = errorMutable

    fun getVideoLink(idContent: Int, contentEnum: ContentEnum) {
        getContentById(
            params = GetContentById.Params(
                id = idContent,
                content = contentEnum
            ),
            scope = viewModelScope
        ){ either ->
            either.fold(
                { failure ->
                    errorMutable.postValue(failure)
                },
                { content ->
                    recentAction(content)
                    contentMutable.postValue(content)
                }
            )
        }
    }
    private fun recentAction(content: Content?){
        if(content == null) return
        getRecentById(
            params = GetRecentById.Param(
                contentId = content.id,
                contentEnum = content.contentType
            ),
            scope = viewModelScope
        ){ either ->
            either.fold(
                { either ->
                    errorMutable.postValue(either)
                },
                { recent ->
                    if(recent == null){
                        addRecent(content = content)
                    }else{
                        updateRecent(recent = recent)
                    }
                }
            )
        }
    }
    private fun addRecent(content: Content){
        addRecentContent(
            params = AddRecentContent.Param(
                recent = Recent(
                    idContent = content.id,
                    contentType = content.contentType,
                    posterPreview = content.posterPreview,
                    time = HelperFunction.getSecondTime()
                )
            ),
            scope = viewModelScope
        ){ either ->
            either.fold(
                { failure ->
                    errorMutable.postValue(failure)
                },{}
            )
        }
    }
    private fun updateRecent(recent: Recent){
        updateRecentContent(
            params = UpdateRecentContent.Param(
                recent = recent.apply {
                    time = HelperFunction.getSecondTime()
                }
            ),
            scope = viewModelScope
        ){ either ->
            either.fold(
                { failure ->
                  errorMutable.postValue(failure)
                },
                {}
            )
        }
    }
    class Factory @Inject constructor(
        val getContentById: GetContentById,
        val getRecentById: GetRecentById,
        val addRecentContent: AddRecentContent,
        val updateRecentContent: UpdateRecentContent
    ): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return WebViewVM(
                getContentById = getContentById,
                getRecentById = getRecentById,
                addRecentContent = addRecentContent,
                updateRecentContent = updateRecentContent
            ) as T
        }

    }
}