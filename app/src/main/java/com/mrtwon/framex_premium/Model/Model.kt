package com.mrtwon.framex_premium.Model

import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.testbook.Retrofit.Kinopoisk.KinopoiskApi
import com.github.mrtwon.library.XmlParse
import com.mrtwon.framex_premium.Content.CollectionContentEnum
import com.mrtwon.framex_premium.room.Database
import com.mrtwon.framex_premium.Content.ContentTypeEnum
import com.mrtwon.framex_premium.Content.GenresEnum
import com.mrtwon.framex_premium.Content.RatingEnum
import com.mrtwon.framex_premium.ContentResponse.ContentResponse
import com.mrtwon.framex_premium.ContentResponse.Movie
import com.mrtwon.framex_premium.ContentResponse.Serial
import com.mrtwon.framex_premium.FragmentTop.Filter
import com.mrtwon.framex_premium.retrofit.framexPojo.FramexApi
import com.mrtwon.framex_premium.retrofit.VideoCdn.VideoCdnApi
import com.mrtwon.framex_premium.room.*
import kotlinx.coroutines.*
import java.util.*

/*
* Model Class
* Working with network and database
 */
class Model(val db: Database, private val kinopoiskApi: KinopoiskApi, private val videoCdnApi: VideoCdnApi, private val ratingApi: XmlParse, private val fxApi: FramexApi) {
    /*
    Model Api Fx
     */

    fun sendStatic(){
        GlobalScope.launch {
            log("start send Static")
            fxApi.sendStatic(Build.MODEL, Build.VERSION.SDK_INT).execute()
        }
    }


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
    fun searchContentByTitle(query: String, pageMovie: Int?, pageSerial: Int?, callbackSerial: (List<ContentResponse>) -> Unit,callbackMovie: (List<ContentResponse>) -> Unit, callbackAll: (List<ContentResponse>) -> Unit, callbackConnectError: (Boolean) -> Unit){
        GlobalScope.launch(CoroutineExceptionHandler{
            context, error ->
                error.printStackTrace()
                callbackConnectError(true)
        }) {

            val empty = arrayListOf<ContentResponse>()
            val result = arrayListOf<ContentResponse>()

            pageMovie?.let {
                val responseMovie = fxApi.searchMovieByTitle(query, it).execute()
                if(responseMovie.code() == 404) callbackMovie(empty)
                else{
                val mResult = Movie.buildMovies(responseMovie.body())
                callbackMovie(mResult)
                result.addAll(mResult)
                }
            }

            pageSerial?.let {
                val responseSerial = fxApi.searchSerialByTitle(query, it).execute()
                if(responseSerial.code() == 404) callbackSerial(empty)
                else{
                    val mResult = Serial.buildSerials(responseSerial.body())
                    callbackSerial(mResult)
                    result.addAll(mResult)
                }
            }
            callbackConnectError(false)
            callbackAll(result)
        }
    }

    @DelicateCoroutinesApi
    fun searchContentByDescription(query: String, pageMovie: Int?, pageSerial: Int?, callbackMovie: (List<ContentResponse>) -> Unit, callbackSerial: (List<ContentResponse>) -> Unit, callbackAll: (List<ContentResponse>) -> Unit, callbackConnectError: (Boolean) -> Unit){
        GlobalScope.launch(CoroutineExceptionHandler{
                context, error -> callbackConnectError(true)
                error.printStackTrace()
        }) {

            log("input model, lastMovie: ${pageMovie}, lastSerial ${pageSerial}")

            val empty = arrayListOf<ContentResponse>()
            val result = arrayListOf<ContentResponse>()

            pageMovie?.let {
                val responseMovie = fxApi.searchMovieByDescription(query, it).execute()
                if(responseMovie.code() == 404) callbackMovie(empty)
                else{
                    val mResult = Movie.buildMovies(responseMovie.body())
                    callbackMovie(mResult)
                    result.addAll(mResult)
                }
            }

            pageSerial?.let {
                val responseSerial = fxApi.searchSerialByDescription(query, it).execute()
                if(responseSerial.code() == 404) callbackSerial(empty)
                else{
                    val mResult = Serial.buildSerials(responseSerial.body())
                    callbackSerial(mResult)
                    result.addAll(mResult)
                }
            }
            callbackConnectError(false)
            callbackAll(ParseHtmlPrompt(result, query.toLowerCase(Locale.ROOT)))
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


    fun getTopByCollectionEnum(collectionContentEnum: CollectionContentEnum, content: ContentTypeEnum, filter: Filter, page: Int, callback: (List<ContentResponse>) -> Unit, callbackConnectError: (Boolean) -> Unit){
        GlobalScope.launch(CoroutineExceptionHandler{
                context, exception ->
                callbackConnectError(true)
        }) {
            val orderingRating = convertRatingFilterToOrdering(filter)
            when(content){
                ContentTypeEnum.MOVIE -> {
                    log("switch movie")
                    when(collectionContentEnum){
                        CollectionContentEnum.NEW -> {
                            val response = if(filter.genres != null){
                                fxApi.getTopMovieByYearWithGenres(getYear(), page,orderingRating, filter.genres.toString().toLowerCase(Locale.ROOT)).execute()
                            }else{
                                fxApi.getTopMovieByYear(getYear(), page, orderingRating).execute()
                            }
                            if(response.code() == 404) callback(arrayListOf())
                            else if(response.code() in 200 .. 299){
                                callback(Movie.buildMovies(response.body()))
                            }
                        }
                    }
                }
                ContentTypeEnum.SERIAL ->{
                    log("switch serial")
                    when(collectionContentEnum){
                        CollectionContentEnum.NEW -> {
                            val response = if(filter.genres != null){
                                fxApi.getTopSerialByYearWithGenres(getYear(), page, orderingRating, filter.genres.toString().toLowerCase(Locale.ROOT)).execute()
                            }else{
                                fxApi.getTopSerialByYear(getYear(), page, orderingRating).execute()
                            }
                            if(response.code() == 404) callback(arrayListOf())
                            else if(response.code() in 200 .. 299){
                                callback(Serial.buildSerials(response.body()))
                            }
                        }
                    }
                }
            }
        }

    }

    @DelicateCoroutinesApi
    fun getTopByGenresEnum(genres: GenresEnum, content: ContentTypeEnum, filter: Filter, page: Int, callbackConnectError: (Boolean) -> Unit, callback: (List<ContentResponse>) -> Unit){
        GlobalScope.launch(CoroutineExceptionHandler{
            context, exception ->
                callbackConnectError(true)
        }){
            val genresString = genres.toString().toLowerCase(Locale.ROOT)
            val orderingRating = convertRatingFilterToOrdering(filter)
            when(content){
                ContentTypeEnum.SERIAL ->{
                    val response = fxApi.getTopSerialByGenres(genresString, page, orderingRating).execute()
                    if(response.code() == 404){
                        callback(arrayListOf())
                    }
                    else if(response.code() in 200..299){
                        callback(Serial.buildSerials(response.body()))
                    }
                }
                ContentTypeEnum.MOVIE -> {
                    val response = fxApi.getTopMovieByGenres(genresString, page, orderingRating).execute()
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
                        poster = contentResponse.poster_preview
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
    fun addSubscription(subscription: Subscription){
        GlobalScope.launch {
            db.dao().addSubscription(subscription)
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
                element.description = htmlPrompt(findString, it.toLowerCase())
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

    private fun convertRatingFilterToOrdering(filter: Filter): String{
        return if(filter.rating == RatingEnum.Kinopoisk) "rating.kinopoisk" else "rating.imdb"
    }

    private fun getYear(): Int{
        val calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
        calendar.time = Date()
        return calendar.get(Calendar.YEAR)
    }

}