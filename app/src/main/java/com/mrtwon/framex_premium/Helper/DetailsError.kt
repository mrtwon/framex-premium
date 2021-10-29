package com.mrtwon.framex_premium.Helper

import com.mrtwon.framex_premium.retrofit.framexAuth.ResponseError
import java.lang.Exception

data class DetailsError(val type: TYPE_ERROR = TYPE_ERROR.ERROR, val title: String, val message: String) {
    companion object{
        fun fromResponseError(type: TYPE_ERROR = TYPE_ERROR.ERROR, response: ResponseError): DetailsError{
            return DetailsError(type, response.response.title, response.response.message)
        }
        fun fromException(type: TYPE_ERROR = TYPE_ERROR.ERROR, exception: Exception?): DetailsError{
            return DetailsError(type, "Ошибка", exception?.message ?: "")
        }
    }
}
enum class TYPE_ERROR { FALIED, ERROR }