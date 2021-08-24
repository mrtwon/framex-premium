package com.mrtwon.framex_premium.retrofit.testPOJO

import com.mrtwon.framex_premium.retrofit.testPOJO.responseMovie.ResponseMovie
import com.mrtwon.framex_premium.retrofit.testPOJO.responseSerial.ResponseSerial
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FramexApi {
  @GET("api/serial?ordering=rating.kinopoisk&direction=desc&limit=20")
  fun getTopSerialByGenres(@Query("genres") genres: String, @Query("page") page: Int): Call<ResponseSerial>

  @GET("api/movie?ordering=rating.kinopoisk&direction=desc&limit=20")
  fun getTopMovieByGenres(@Query("genres") genres: String, @Query("page") page: Int): Call<ResponseMovie>

  @GET("api/movie/year?ordering=rating.kinopoisk&direction=desc&limit=20")
  fun getTopMovieByYear(@Query("year") year: Int, @Query("page") page: Int): Call<ResponseMovie>

  @GET("api/serial/year?ordering=rating.kinopoisk&direction=desc&limit=20")
  fun getTopSerialByYear(@Query("year") year: Int, @Query("page") page: Int): Call<ResponseSerial>


  @GET("api/serial/id")
  fun getAboutSerial(@Query("id") id: Int): Call<ResponseSerial>

  @GET("api/movie/id")
  fun getAboutMovie(@Query("id") id: Int): Call<ResponseMovie>


  // search by title
  @GET("api/movie/query?limit=20?ordering=rating.kinopoisk&direction=desc")
  fun searchMovieByTitle(@Query("title") title: String, @Query("page") page: Int): Call<ResponseMovie>
  @GET("api/serial/query?limit=20?ordering=rating.kinopoisk&direction=desc")
  fun searchSerialByTitle(@Query("title") title: String, @Query("page") page: Int): Call<ResponseSerial>

  //search by description
  @GET("api/movie/query?ordering=rating.kinopoisk&direction=desc&limit=20")
  fun searchMovieByDescription(@Query("description") description: String, @Query("page") page: Int): Call<ResponseMovie>
  @GET("api/serial/query?ordering=rating.kinopoisk&direction=desc&limit=20")
  fun searchSerialByDescription(@Query("description") description: String, @Query("page") page: Int): Call<ResponseSerial>

  @GET("api/static")
  fun sendStatic(@Query("model") model: String, @Query("api") api: Int): Call<Unit>
}