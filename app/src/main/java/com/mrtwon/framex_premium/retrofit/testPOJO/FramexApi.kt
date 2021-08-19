package com.mrtwon.framex_premium.retrofit.testPOJO

import com.mrtwon.framex_premium.retrofit.testPOJO.responseMovie.ResponseMovie
import com.mrtwon.framex_premium.retrofit.testPOJO.responseSerial.ResponseSerial
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FramexApi {
  @GET("api/serial?ordering=rating.imdb&direction=desc&limit=20")
  fun getTopSerialByGenres(@Query("genres") genres: String, @Query("page") page: Int): Call<ResponseSerial>

  @GET("api/movie?ordering=rating.imdb&direction=desc&limit=20")
  fun getTopMovieByGenres(@Query("genres") genres: String, @Query("page") page: Int): Call<ResponseMovie>

  @GET("api/movie/year?ordering=rating.imdb&direction=desc&limit=20")
  fun getTopMovieByYear(@Query("year") year: Int, @Query("page") page: Int): Call<ResponseMovie>

  @GET("api/serial/year?ordering=rating.imdb&direction=desc&limit=20")
  fun getTopSerialByYear(@Query("year") year: Int, @Query("page") page: Int): Call<ResponseSerial>


  @GET("api/serial/id")
  fun getAboutSerial(@Query("id") id: Int): Call<ResponseSerial>

  @GET("api/movie/id")
  fun getAboutMovie(@Query("id") id: Int): Call<ResponseMovie>


  // search by title
  @GET("api/movie/query")
  fun searchMovieByTitle(@Query("title") title: String): Call<ResponseMovie>
  @GET("api/serial/query")
  fun searchSerialByTitle(@Query("title") title: String): Call<ResponseSerial>

  //search by description
  @GET("api/movie/query")
  fun searchMovieByDescription(@Query("description") description: String, @Query("limit") limit: Int = 20): Call<ResponseMovie>
  @GET("api/serial/query")
  fun searchSerialByDescription(@Query("description") description: String, @Query("limit") limit: Int = 20): Call<ResponseSerial>

}