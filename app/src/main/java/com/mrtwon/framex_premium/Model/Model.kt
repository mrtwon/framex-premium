package com.mrtwon.framex_premium.Model

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.testbook.Retrofit.Kinopoisk.KinopoiskApi
import com.github.mrtwon.library.XmlParse
import com.mrtwon.framex_premium.Content.CollectionContentEnum
import com.mrtwon.framex_premium.room.Database
import com.mrtwon.framex_premium.Content.ContentTypeEnum
import com.mrtwon.framex_premium.Content.GenresEnum
import com.mrtwon.framex_premium.ContentResponse.ContentResponse
import com.mrtwon.framex_premium.ContentResponse.Movie
import com.mrtwon.framex_premium.ContentResponse.Serial
import com.mrtwon.framex_premium.retrofit.testPOJO.FramexApi
import com.mrtwon.framex_premium.retrofit.testPOJO.responseMovie.ResponseMovie
import com.mrtwon.framex_premium.retrofit.testPOJO.responseSerial.ResponseSerial
import com.mrtwon.framex_premium.retrofit.VideoCdn.VideoCdnApi
import com.mrtwon.framex_premium.room.*
import kotlinx.coroutines.*
import retrofit2.Retrofit
import java.util.*

/*
* Model Class
* Working with network and database
 */
class Model(val db: Database, private val kinopoiskApi: KinopoiskApi, private val videoCdnApi: VideoCdnApi, private val ratingApi: XmlParse, private val fxApi: FramexApi) {
    /*
    Model Api Fx
     */


    fun getContentResponse(id: Int, contentType: String, callback: (ContentResponse?) -> Unit){
        GlobalScope.launch {
            when(contentType){
                "movie" -> {
                    val response = fxApi.getAboutMovie(id).execute().body()
                    callback(Movie.buildMovie(response))
                }
                "tv_series" -> {
                    val response = fxApi.getAboutSerial(id).execute().body()
                    callback(Serial.buildSerial(response))
                }
            }
        }
    }


    @DelicateCoroutinesApi
    fun searchContentByTitle(query: String, callback: (List<ContentResponse>) -> Unit, callbackConnectError: (Boolean) -> Unit){
        GlobalScope.launch(CoroutineExceptionHandler{
            context, error -> callbackConnectError(true)
        }) {
            val callbackResult = arrayListOf<ContentResponse>()
            val responseMovie = fxApi.searchMovieByTitle(query).execute()
            val responseSerial = fxApi.searchSerialByTitle(query).execute()
            if (responseMovie.code() == 404 && responseSerial.code() == 404) callback(callbackResult)
            else {
                val resultSerial = Serial.buildSerials(responseSerial.body())
                val resultMovie = Movie.buildMovies(responseMovie.body())
                callbackResult.addAll(resultMovie)
                callbackResult.addAll(resultSerial)
                callback(callbackResult)
            }
        }
    }

    @DelicateCoroutinesApi
    fun searchContentByDescription(query: String, callback: (List<ContentResponse>) -> Unit, callbackConnectError: (Boolean) -> Unit){
        GlobalScope.launch(CoroutineExceptionHandler{
                context, error -> callbackConnectError(true)
        }) {
            val queryLowerCase = query.toLowerCase(Locale.ROOT)
            val callbackResult = arrayListOf<ContentResponse>()
            val responseMovie = fxApi.searchMovieByDescription(queryLowerCase).execute()
            val responseSerial = fxApi.searchSerialByDescription(queryLowerCase).execute()
            if (responseMovie.code() == 404 && responseSerial.code() == 404) callback(callbackResult)
            else {
                val resultSerial = Serial.buildSerials(responseSerial.body())
                val resultMovie = Movie.buildMovies(responseMovie.body())
                callbackResult.addAll(resultMovie)
                callbackResult.addAll(resultSerial)
                callback(ParseHtmlPrompt(callbackResult, queryLowerCase))
            }
        }
    }

    @DelicateCoroutinesApi
    fun getAboutMovie(id: Int, callback: (Movie?) -> Unit, callbackError: (Boolean) -> Unit){
        GlobalScope.launch(CoroutineExceptionHandler{ context, error ->
            callbackError(true)
        }) {
            val response = fxApi.getAboutMovie(id).execute().body()
            val result = Movie.buildMovie(response)
            callback(result)
        }
    }

    @DelicateCoroutinesApi
    fun getAboutSerial(id: Int, callback: (Serial?) -> Unit, callbackError: (Boolean) -> Unit){
        GlobalScope.launch(CoroutineExceptionHandler{
            context, exception -> callbackError(true)
        }) {
            val response = fxApi.getAboutSerial(id).execute().body()
            callback(Serial.buildSerial(response))
        }
    }


    fun getTopByCollectionEnum(collectionContentEnum: CollectionContentEnum, content: ContentTypeEnum, page: Int, callback: (List<ContentResponse>) -> Unit, callbackConnectError: (Boolean) -> Unit){
        GlobalScope.launch(CoroutineExceptionHandler{
                context, exception ->
                callbackConnectError(true)
        }) {
            when(content){
                ContentTypeEnum.MOVIE -> {
                    when(collectionContentEnum){
                        CollectionContentEnum.NEW -> {
                            val response = fxApi.getTopMovieByYear(getYear(), page).execute().body()
                            callback(Movie.buildMovies(response))
                        }
                    }
                }
                ContentTypeEnum.SERIAL ->{
                    when(collectionContentEnum){
                        CollectionContentEnum.NEW -> {
                            val response = fxApi.getTopSerialByYear(getYear(), page).execute().body()
                            callback(Serial.buildSerials(response))
                        }
                    }
                }
            }
        }

    }

    @DelicateCoroutinesApi
    fun getTopByGenresEnum(genres: GenresEnum, content: ContentTypeEnum, page: Int, callbackConnectError: (Boolean) -> Unit, callback: (List<ContentResponse>) -> Unit){
        GlobalScope.launch(CoroutineExceptionHandler{
            context, exception ->
                callbackConnectError(true)
        }){
            when(content){
                ContentTypeEnum.SERIAL ->{
                    val response = fxApi.getTopSerialByGenres(genres.toString(), page).execute()
                    if(response.code() == 404){
                        callback(arrayListOf())
                    }
                    else if(response.code() in 200..299){
                        callback(Serial.buildSerials(response.body()))
                    }
                }
                ContentTypeEnum.MOVIE -> {
                    val response = fxApi.getTopMovieByGenres(genres.toString(), page).execute()
                    if(response.code() == 404){
                        callback(arrayListOf())
                    }
                    else if(response.code() in 200..299){
                        callback(Movie.buildMovies(response.body()))
                    }
                }
            }
        }
    }


    //helper function

    //log
    private fun log(s: String) { Log.i("self-model",s) }




    fun checkedBlockSync(id: Int, contentType: String): Boolean{
        when(contentType){
            "tv_series" -> {
                return videoCdnApi.serialById(id).execute().body()?.data?.isEmpty() ?: true
            }
            "movie" -> {
                return videoCdnApi.movieById(id).execute().body()?.data?.isEmpty() ?: true
            }
        }
        return false
    }


    /*
    Model Database
     */


    fun getFavorite(callback: (List<Favorite>) -> Unit) {
        GlobalScope.launch {
            val listFavorite = db.dao().getFavorite()
            callback(listFavorite)
        }
    }


    fun favoriteAction(content: ContentResponse){
        GlobalScope.launch {
            val isFavorite = db.dao().getFavoriteElement(content.id, content.contentType) != null
            if(isFavorite){
                db.dao().deleteFavoriteElement(content.id, content.contentType)
            }else{
                db.dao().addFavorite(Favorite().apply {
                    id_content = content.id
                    poster = content.poster
                    ru_title = content.ru_title
                    content_type = content.contentType
                })
            }
        }
    }


    fun removeFavorite(favorite: Favorite) {
        GlobalScope.launch {
            db.dao().deleteFavorite(favorite)
        }
    }


    fun addRecent(contentResponse: ContentResponse?) {
        GlobalScope.launch {
            if (contentResponse != null) {
                if(isRecent(contentResponse.id, contentResponse.contentType)){
                    val recent = db.dao().getRecentElement(contentResponse.id, contentResponse.contentType)
                    if(recent != null){
                        recent.time = getSecondTime()
                        db.dao().updateRecent(recent)
                    }
                }else{
                    val result = Recent().apply {
                        id_content = contentResponse.id
                        content_type = contentResponse.contentType
                        poster = contentResponse.poster
                        time = getSecondTime()
                    }
                    db.dao().insertRecent(result)
                }
            }
        }
    }

    fun isRecent(id: Int, contentType: String): Boolean{
        return db.dao().getRecentElement(id, contentType) != null
    }
    fun getRecentList(callback: (List<Recent>) -> Unit){
        GlobalScope.launch {
            val recentList = db.dao().getRecentList()
            callback(recentList)
        }
    }




    fun getNotificationList(callback: (List<Notification>) -> Unit){
        GlobalScope.launch(Dispatchers.IO) {
            val result = db.dao().getNotification()
            callback(result)
        }
    }
    fun getNotificationListLiveData(): LiveData<List<Notification>> = db.dao().getNotificationLiveData()
    fun getSubscriptionListLiveData(): LiveData<List<Subscription>> = db.dao().getSubscriptionsLiveData()


    fun removeNotification(notification: Notification){
        GlobalScope.launch {
            val result = db.dao().deleteNotification(notification)
        }
    }

    fun removeSubscription(subscription: Subscription){
        GlobalScope.launch {
           db.dao().deleteSubscription(subscription)
        }
    }
    fun subscriptionAction(contentResponse: ContentResponse){
        GlobalScope.launch {
            val isExisting = db.dao().getSubscriptionById(contentResponse.id)
            if(isExisting == null){
                val count = allCountEpisodeSerialSync(contentResponse.id)
                if(count != null){
                    db.dao().addSubscription(Subscription().apply {
                        this.count = count
                        this.content_id = contentResponse.id
                        this.poster = contentResponse.poster
                    })
                }
            }else{ db.dao().deleteSubscriptionById(contentResponse.id) }
        }
    }
    fun getSubscriptionByIdLiveData(id: Int): LiveData<Subscription> = db.dao().getSubscriptionByIdLiveData(id)
    fun allCountEpisodeSerialSync(id: Int): Int?{
        val response = videoCdnApi.serialById(id).execute().body()
        return response?.data?.get(0)?.episodeCount
    }


    /*
    Helper Functions
     */

    fun ParseHtmlPrompt(contents: List<ContentResponse>, findString: String): List<ContentResponse>{
        val result = arrayListOf<ContentResponse>()
        for(element in contents){
            element.description?.let {
                element.description = htmlPrompt(findString, it)
            }
            result.add(element)
        }
        return result
    }

    fun htmlPrompt(findString: String, data: String): String {
        return data.replace(findString, "<b>$findString</b>")
    }

    private fun getSecondTime(): Int{
        return (Date().time/1000).toInt()
    }

    private fun getYear(): Int{
        val calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
        calendar.time = Date()
        return calendar.get(Calendar.YEAR)+1
    }

}