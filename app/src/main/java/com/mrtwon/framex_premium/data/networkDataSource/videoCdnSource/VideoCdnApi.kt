package com.mrtwon.framex_premium.data.networkDataSource.videoCdnSource

import com.mrtwon.framex_premium.data.networkDataSource.videoCdnSource.ContentResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface VideoCdnApi {
    @GET("tv-series?api_token=vUkLJNDY2q8mfEfICiSzzsFsnCqgSdHR")
    fun getTvContentById(@Query("id") id: Int): Call<ContentResponse>
}