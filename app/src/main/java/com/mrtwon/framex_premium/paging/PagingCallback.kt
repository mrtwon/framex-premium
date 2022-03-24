package com.mrtwon.testfirebase.paging

import com.mrtwon.framex_premium.domain.exception.Failure

interface PagingCallback {
    fun onFirstLoad(state: Boolean)
    fun onLoad(state: Boolean)
    fun onError(failure: Failure)
    fun onSuccessful(state: Boolean)
    fun onNotFound(state: Boolean)
}