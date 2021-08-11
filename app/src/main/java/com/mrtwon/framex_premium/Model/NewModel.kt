package com.mrtwon.framex_premium.Model

import android.util.Log
import com.mrtwon.framex_premium.Content.ContentTypeEnum
import com.mrtwon.framex_premium.Content.GenresEnum
import com.mrtwon.framex_premium.ContentResponse.Content
import com.mrtwon.framex_premium.ContentResponse.Movie
import com.mrtwon.framex_premium.ContentResponse.Serial
import com.mrtwon.framex_premium.Retrofit.TestPOJO.FramexApi
import com.mrtwon.framex_premium.Retrofit.TestPOJO.ResponseMovie.ResponseMovie
import com.mrtwon.framex_premium.Retrofit.TestPOJO.ResponseSerial.ResponseSerial
import com.mrtwon.framex_premium.room.Database
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NewModel(val database: Database, val fxApi: FramexApi) {
    @DelicateCoroutinesApi
    fun getAboutMovie(id: Int, callback: (Movie?) -> Unit){
        GlobalScope.launch {
            val response = fxApi.getAboutMovie(id).execute().body()
            val result = Movie.buildMovie(response?.response?.get(0))
            callback(result)
        }
    }

    @DelicateCoroutinesApi
    fun getAboutSerial(id: Int, callback: (Serial?) -> Unit){
        GlobalScope.launch {
            val response = fxApi.getAboutSerial(id).execute().body()
            val result = Serial.buildSerial(response?.response?.get(0))
            callback(result)
        }
    }

    @DelicateCoroutinesApi
    fun getTopByGenresEnumTest(genres: GenresEnum, content: ContentTypeEnum, callback: (List<Content>) -> Unit){
        log("start gettop ..., contentType: ${content.toString()}")
        GlobalScope.launch {
            when(content){
                ContentTypeEnum.SERIAL ->{
                    val response = fxApi.getTopSerialByGenres(genres.toString()).execute().body()
                    val result = giveListContentFromResponseSerial(response)
                    callback(result)
                }
                ContentTypeEnum.MOVIE -> {
                    log("start case")
                    val response = fxApi.getTopMovieByGenres(genres.toString()).execute()
                    val result = giveListContentFromResponseMovie(response.body())
                    log("result size = ${result.size}")
                    callback(result)
                    log("response code: ${response.code()}")
                }
            }
        }
    }


    //helper function
    private fun giveListContentFromResponseSerial(responseSerial: ResponseSerial?): List<Content>{
        val result = arrayListOf<Content>()
        if(responseSerial == null) return result
        val response = responseSerial.response ?: return result
        for(element in response){
            val content = Serial.buildSerial(element)
            if(content != null) result.add(content)
        }
        return result
    }
    private fun giveListContentFromResponseMovie(responseMovie: ResponseMovie?): List<Content>{
        val result = arrayListOf<Content>()
        if(responseMovie == null) return result
        val response = responseMovie.response ?: return result
        log("responseMovie size = ${response.size}")
        for(element in response){
            val content = Movie.buildMovie(element)
            if(content != null) result.add(content)
        }
        return result
    }
    //log
    private fun log(s: String) { Log.i("self-new-model",s) }
}