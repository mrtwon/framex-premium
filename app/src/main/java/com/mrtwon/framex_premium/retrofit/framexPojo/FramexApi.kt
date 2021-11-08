package com.mrtwon.framex_premium.retrofit.framexPojo

import com.mrtwon.framex_premium.retrofit.framexAuth.ResponseUser
import com.mrtwon.framex_premium.retrofit.framexPojo.responseMovie.ResponseMovie
import com.mrtwon.framex_premium.retrofit.framexPojo.responseSerial.ResponseSerial
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FramexApi {
  @GET("api/serial?direction=desc&limit=20")
  fun getTopSerialByGenres(@Query("genres") genres: String, @Query("page") page: Int, @Query("ordering") ordering: String): Call<ResponseSerial>

  @GET("api/movie?direction=desc&limit=20")
  fun getTopMovieByGenres(@Query("genres") genres: String, @Query("page") page: Int, @Query("ordering") ordering: String): Call<ResponseMovie>

  @GET("api/movie/year?direction=desc&limit=20")
  fun getTopMovieByYear(@Query("year") year: Int, @Query("page") page: Int, @Query("ordering") ordering: String): Call<ResponseMovie>
  @GET("api/movie/year?direction=desc&limit=20")
  fun getTopMovieByYearWithGenres(@Query("year") year: Int, @Query("page") page: Int, @Query("ordering") ordering: String, @Query("genres") genres: String): Call<ResponseMovie>

  @GET("api/serial/year?&direction=desc&limit=20")
  fun getTopSerialByYearWithGenres(@Query("year") year: Int, @Query("page") page: Int, @Query("ordering") ordering: String, @Query("genres") genres: String): Call<ResponseSerial>
  @GET("api/serial/year?direction=desc&limit=20")
  fun getTopSerialByYear(@Query("year") year: Int, @Query("page") page: Int, @Query("ordering") ordering: String): Call<ResponseSerial>


  @GET("api/serial/id")
  fun getAboutSerial(@Query("id") id: Int): Call<ResponseSerial>

  @GET("api/movie/id")
  fun getAboutMovie(@Query("id") id: Int): Call<ResponseMovie>


  // search by title
  @GET("api/movie/query?limit=20&ordering=rating.kinopoisk&direction=desc")
  fun searchMovieByTitle(@Query("title") title: String, @Query("page") page: Int): Call<ResponseMovie>
  @GET("api/serial/query?limit=20&ordering=rating.kinopoisk&direction=desc")
  fun searchSerialByTitle(@Query("title") title: String, @Query("page") page: Int): Call<ResponseSerial>

  //search by description
  @GET("api/movie/query?ordering=rating.kinopoisk&direction=desc&limit=20")
  fun searchMovieByDescription(@Query("description") description: String, @Query("page") page: Int): Call<ResponseMovie>
  @GET("api/serial/query?ordering=rating.kinopoisk&direction=desc&limit=20")
  fun searchSerialByDescription(@Query("description") description: String, @Query("page") page: Int): Call<ResponseSerial>

  @GET("api/static")
  fun sendStatic(@Query("model") model: String, @Query("api") api: Int): Call<Unit>

  // fx api auth
  @GET("api/auth/createUser")
  fun sendCreateUserRequest(@Query("token") token: String) : Call<Unit>
  @GET("api/auth/giveMe")
  fun giveMeProfile(@Query("token") token: String) : Call<ResponseUser>
  @GET("api/auth/createNickName")
  fun createNickName(@Query("token") token: String, @Query("nickname") nickName: String): Call<Unit>
  @GET("api/auth/createAboutMe")
  fun createAboutMe(@Query("token") token: String, @Query("about") about: String): Call<Unit>
  @GET("api/auth/createFavoriteContent")
  fun createFavorite(@Query("token") token: String, @Query("favorite") favorite: String): Call<Unit>
  @GET("api/auth/createFavoriteStatus")
  fun createOpenFavorite(@Query("token") token: String, @Query("status") status: String): Call<Unit>
  @GET("api/auth/createPhoto")
  fun createPhoto(@Query("token") token: String, @Query("photo") photoUrl: String): Call<Unit>

  @GET("ping")
  fun pingFxServer(): Call<Unit>
}