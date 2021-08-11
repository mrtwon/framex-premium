package com.mrtwon.framex_premium.Retrofit.TestPOJO

import com.mrtwon.framex_premium.Retrofit.TestPOJO.ResponseMovie.ResponseMovie
import com.mrtwon.framex_premium.Retrofit.TestPOJO.ResponseSerial.ResponseSerial
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FramexApi {
  @GET("serial?ordering=rating.imdb&direction=desc&limit=20")
  fun getTopSerialByGenres(@Query("genres") genres: String): Call<ResponseSerial>

  @GET("movie?ordering=rating.imdb&direction=desc&limit=20")
  fun getTopMovieByGenres(@Query("genres") genres: String): Call<ResponseMovie>

  @GET("serial")
  fun getAboutSerial(@Query("id") id: Int): Call<ResponseSerial>

  @GET("movie")
  fun getAboutMovie(@Query("id") id: Int): Call<ResponseMovie>


}