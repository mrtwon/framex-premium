package com.mrtwon.framex_premium.Model

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.testbook.Retrofit.Kinopoisk.KinopoiskApi
import com.github.mrtwon.library.XmlParse
import com.mrtwon.framex_premium.room.Database
import com.mrtwon.framex_premium.Content.CollectionContentEnum
import com.mrtwon.framex_premium.Content.ContentTypeEnum
import com.mrtwon.framex_premium.Content.GenresEnum
import com.mrtwon.framex_premium.ContentResponse.ContentResponse
import com.mrtwon.framex_premium.Retrofit.Kinopoisk.POJOKinopoisk
import com.mrtwon.framex_premium.Retrofit.KinopoiskRating.RatingApi
import com.mrtwon.framex_premium.Retrofit.KinopoiskRating.RatingPOJO
import com.mrtwon.framex_premium.Retrofit.TestPOJO.FramexApi
import com.mrtwon.framex_premium.Retrofit.TestPOJO.ResponseMovie.ResponseMovie
import com.mrtwon.framex_premium.Retrofit.TestPOJO.ResponseSerial.ResponseSerial
import com.mrtwon.framex_premium.Retrofit.VideoCdn.VideoCdnApi
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

    @DelicateCoroutinesApi
    fun searchContentByTitle(query: String, callback: (List<ContentResponse>) -> Unit){
        GlobalScope.launch {
            val callbackResult = arrayListOf<ContentResponse>()
            val responseMovie = fxApi.searchMovieByTitle(query)
            val responseSerial = fxApi.searchSerialByTitle(query)
            val resultSerial = giveListContentFromResponseSerial(responseSerial.execute().body())
            val resultMovie = giveListContentFromResponseMovie(responseMovie.execute().body())
            callbackResult.addAll(resultMovie)
            callbackResult.addAll(resultSerial)
            callback(callbackResult)
        }
    }

    @DelicateCoroutinesApi
    fun searchContentByDescription(query: String, callback: (List<ContentResponse>) -> Unit){
        GlobalScope.launch {
            val queryLowerCase = query.toLowerCase(Locale.ROOT)
            val callbackResult = arrayListOf<ContentResponse>()
            val responseMovie = fxApi.searchMovieByDescription(queryLowerCase)
            val responseSerial = fxApi.searchSerialByDescription(queryLowerCase)
            val resultSerial = giveListContentFromResponseSerial(responseSerial.execute().body())
            val resultMovie = giveListContentFromResponseMovie(responseMovie.execute().body())
            callbackResult.addAll(resultMovie)
            callbackResult.addAll(resultSerial)
            callback(ParseHtmlPrompt(callbackResult, queryLowerCase))
        }
    }

    @DelicateCoroutinesApi
    fun getAboutMovie(id: Int, callback: (com.mrtwon.framex_premium.ContentResponse.Movie?) -> Unit){
        GlobalScope.launch {
            val response = fxApi.getAboutMovie(id).execute().body()
            val result = com.mrtwon.framex_premium.ContentResponse.Movie.buildMovie(response?.response?.get(0))
            callback(result)
        }
    }

    @DelicateCoroutinesApi
    fun getAboutSerial(id: Int, callback: (com.mrtwon.framex_premium.ContentResponse.Serial?) -> Unit){
        GlobalScope.launch {
            val response = fxApi.getAboutSerial(id).execute().body()
            val result = com.mrtwon.framex_premium.ContentResponse.Serial.buildSerial(response?.response?.get(0))
            callback(result)
        }
    }

    @DelicateCoroutinesApi
    fun getTopByGenresEnum(genres: GenresEnum, content: ContentTypeEnum, callback: (List<ContentResponse>) -> Unit){
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
    private fun giveListContentFromResponseSerial(responseSerial: ResponseSerial?): List<ContentResponse>{
        val result = arrayListOf<ContentResponse>()
        if(responseSerial == null) return result
        val response = responseSerial.response ?: return result
        for(element in response){
            val content = com.mrtwon.framex_premium.ContentResponse.Serial.buildSerial(element)
            if(content != null) result.add(content)
        }
        return result
    }
    private fun giveListContentFromResponseMovie(responseMovie: ResponseMovie?): List<ContentResponse>{
        val result = arrayListOf<ContentResponse>()
        if(responseMovie == null) return result
        val response = responseMovie.response ?: return result
        log("responseMovie size = ${response.size}")
        for(element in response){
            val content = com.mrtwon.framex_premium.ContentResponse.Movie.buildMovie(element)
            if(content != null) result.add(content)
        }
        return result
    }
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

    //log
    private fun log(s: String) { Log.i("self-model",s) }


    fun searchSerial(query: String, callback: (List<Serial>) -> Unit, noConnect: (Boolean) -> Unit){
        GlobalScope.launch(CoroutineExceptionHandler { context, exception ->
            noConnect(true)
        }) {
            val responseListSerials =
                videoCdnApi.searchSerial(query).execute().body()?.data
            val result = arrayListOf<Serial>()
            if (responseListSerials != null) {
                for (element in responseListSerials) {
                    if (element?.id != null && element.contentType != null) {
                        if (!isExistingSync(element.id, element.contentType)) {
                            continue
                        }
                    } else { continue }
                    element.kinopoiskId?.let {
                        val kp_pojo = kinopoiskApi.filmsInfo(it.toInt()).execute().body()
                        val rating = RatingApi.getRating(it.toInt())
                        result.add(
                            Serial.build(
                                rating, kp_pojo, element
                            )
                        )
                    }
                }
                callback(result)
            }
        }
    }
    fun searchMovie(query: String,callback: (List<Movie>) -> Unit, noConnect: (Boolean) -> Unit) {
        GlobalScope.launch(CoroutineExceptionHandler { context, exception ->
            noConnect(true)
        }) {
            val responseListSerials = videoCdnApi.searchMovie(query).execute().body()?.data
            val result = arrayListOf<Movie>()
            if (responseListSerials != null) {
                for (element in responseListSerials) {
                    if (element?.id != null && element.contentType != null) {
                        if (!isExistingSync(element.id, element.contentType)) {
                            continue
                        }
                    } else {
                        continue
                    }
                    element.kinopoiskId?.let {
                        val kp_pojo = kinopoiskApi.filmsInfo(it.toInt()).execute().body()
                        val rating = RatingApi.getRating(it.toInt())
                        result.add(
                            Movie.build(
                                rating, kp_pojo, element
                            )
                        )
                    }
                }
                callback(result)
            }
        }
    }

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


    fun giveKpSync(kp_id: Int): POJOKinopoisk? = kinopoiskApi.filmsInfo(kp_id).execute().body()
    fun giveRatingSync(kp_id: Int): RatingPOJO = ratingApi.request(kp_id.toString(), RatingPOJO::class.java)



    /*
    Model Database
     */

   /* @DelicateCoroutinesApi
    fun getTopByGenresEnum(genres: GenresEnum, content: ContentTypeEnum, callback: (List<Content>) -> Unit){
        GlobalScope.launch {
            when(content){
                ContentTypeEnum.SERIAL ->{
                    val resultDB = db.dao().getTopSerial(genres.toString())
                    callback(resultDB)
                }
                ContentTypeEnum.MOVIE -> {
                    val resultDB = db.dao().getTopMovie(genres.toString())
                    callback(resultDB)
                }
            }
        }
    }*/
    @DelicateCoroutinesApi
    fun getTopByCollectionEnum(collectionEnum: CollectionContentEnum, content: ContentTypeEnum, callback: (List<Content>) -> Unit){
        GlobalScope.launch {
            val date = Calendar.getInstance().get(Calendar.YEAR)
            when(content){
                ContentTypeEnum.SERIAL -> {
                    val result = db.dao().getTopSerialByCurrentYear(date)
                    callback(result)
                }
                ContentTypeEnum.MOVIE -> {
                    val result = db.dao().getTopMovieByCurrentYear(date)
                    callback(result)
                }
            }
        }
    }
   /* @DelicateCoroutinesApi
    fun getAboutSerial(id: Int, callback: (SerialWithGenres) -> Unit){
        GlobalScope.launch {
            val result = db.dao().getAboutSerial(id)
            Log.i("self-model","description length = ${result.description?.length}")
            callback(result)
        }
    }*/

   /* @DelicateCoroutinesApi
    fun getAboutMovie(id: Int, callback: (MovieWithGenres) -> Unit){
        GlobalScope.launch {
            val result = db.dao().getAboutMovie(id)
            Log.i("self-model","description length = ${result.description?.length}")
            callback(result)
        }
    }*/

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

    fun getVideoLink(id: Int, contentType: String, callback: (Content) -> Unit){
        GlobalScope.launch {
            when (contentType) {
                "tv_series" -> {
                    val result = db.dao().getSerialVideoLink(id)
                    callback(result)
                }
                "movie" -> {
                    val result = db.dao().getMovieVideoLink(id)
                    callback(result)
                }
            }
        }
    }
    fun addRecent(id: Int, contentType: String) {
        GlobalScope.launch {
            val recent: Recent? = db.dao().getRecentElement(id, contentType)
            val time = getSecondTime()
            if (recent == null) {
                val resultRecent = Recent().apply {
                    this.idRef = id
                    this.contentType = contentType
                    this.time = time
                }
                db.dao().insertRecent(resultRecent)
            }else {
                recent.apply {
                    this.time = time
                }
                db.dao().updateRecent(recent)
            }
        }
    }
    fun getRecentList(callback: (List<Recent>) -> Unit){
        GlobalScope.launch {
            val recentList = db.dao().getRecentList()
            callback(recentList)
        }
    }
    fun getRecentContent(callback: (List<Content>) -> Unit) {
        GlobalScope.launch {
            val recentList = db.dao().getRecentList()
            val resultContent = arrayListOf<Content>()
            for (recent in recentList) {
                recent.idRef?.let {
                    val content: Content? = when (recent.contentType) {
                        "tv_series" -> {
                            db.dao().getSerial(it)
                        }
                        "movie" -> {
                            db.dao().getMovie(it)
                        }
                        else -> {
                            null
                        }
                    }

                    if (content != null) {
                        resultContent.add(content)
                    }
                }
            }
            callback(resultContent)
        }
    }
    fun getSizeDatabase(callback: (DatabaseSize) -> Unit){
        GlobalScope.launch {
            val serial = db.dao().getCountSerial().count
            val movie = db.dao().getCountMovie().count
            val result = DatabaseSize(serial, movie)
            callback(result)
        }
    }

    fun getSearchResult(requestString: String, callback: (List<Content>) -> Unit){
        GlobalScope.launch {
            val result = arrayListOf<Content>()
            val stringSearch = "%$requestString%"
            val searchMovie = db.dao().searchTitleMovie(stringSearch)
            val searchSerial = db.dao().searchTitleSerial(stringSearch)
            Log.i("self-search","[$stringSearch] size m = ${searchMovie.size} and s = ${searchSerial.size}")
            result.addAll(searchMovie)
            result.addAll(searchSerial)
            callback(result)
        }
    }
    fun searchDescription(stringSearch: String, callback: (List<Content>) -> Unit){
        GlobalScope.launch {
            val result = arrayListOf<Content>()
            val searchSerial = db.dao().searchDescriptionSerial("%$stringSearch%")
            val searchMovie = db.dao().searchDescriptionMovie("%$stringSearch%")

            result.addAll(searchMovie)
            result.addAll(searchSerial)

            for(elem in result){
                elem.description_lower?.let {
                    elem.description_lower = htmlPrompt(stringSearch, it)
                }
            }

            callback(result)
        }
    }
    fun <T : Content> addListContent(list: List<T>, contentType: String){
        when(contentType){
            "movie" -> {
                db.dao().insertListMovie(list as List<Movie>)
            }
            "tv_series" -> {
                db.dao().insertListSerial(list as List<Serial>)
            }
        }
    }
    fun isExistingSync(id: Int, contentType: String): Boolean{
        return when(contentType){
            "movie" -> {
                return db.dao().isExistingMovie(id) == null
            }
            "tv_series" -> {
                return db.dao().isExistingSerial(id) == null
            }
            else -> false
        }
    }

    fun <T: Genres> addGenresSync(genres: List<T>, contentType: String){
        when(contentType){
            "movie" -> {
                db.dao().addGenresMovie(genres as List<GenresMovie>)
            }
            "tv_series" -> {
                db.dao().addGenresSerial(genres as List<Genres>)
            }
        }
    }
    fun <T : Countries> addCountriesSync(countries: List<T>, contentType: String){
        when(contentType){
            "movie" -> {
                db.dao().addCountriesMovie(countries as List<CountriesMovie>)
            }
            "tv_series" -> {
                db.dao().addCountriesSerial(countries as List<Countries>)
            }
        }
    }
    fun addSerialSync(content: Serial){
        db.dao().addSerial(content)
    }
    fun addMovieSync(content: Movie){
        db.dao().addMovie(content)
    }
    fun <T : Content> createdNewContent(content: T, contentType: String){
        when(contentType){
            "tv_series" ->{
                val pojo_kp = giveKpSync(content.kp_id!!)
                val genres = Genres.buildOther(pojo_kp, content.kp_id!!, content.imdb_id)
                val countries = Countries.buildOther(pojo_kp, content.kp_id, content.imdb_id)
                addCountriesSync(countries, contentType)
                addGenresSync(genres, contentType)
            }
            "movie" -> {
                val pojo_kp = giveKpSync(content.kp_id!!)
                val genres = GenresMovie.buildOther(pojo_kp, content.kp_id!!, content.imdb_id)
                val countries = CountriesMovie.buildOther(pojo_kp, content.kp_id, content.imdb_id)
                addCountriesSync(countries, contentType)
                addGenresSync(genres, contentType)
            }
        }
    }

    fun <T : Content> createdNewContents(content: List<T>, contentType: String){
        for (one_content in content) {
            createdNewContent(one_content, contentType)
            addListContent(content, contentType)
        }
    }
    fun <T : Content>createNewContentFromMix(serial: List<T>?, movie: List<T>?, resultBoolean: (Boolean) -> Unit){
        GlobalScope.launch(CoroutineExceptionHandler { context, exception ->
            resultBoolean(false)
        }){
            serial?.let {
                createdNewContents(serial, "tv_series")
            }
            movie?.let {
                createdNewContents(movie, "movie")
            }
            resultBoolean(true)
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

    fun getSubscriptionList(callback: (List<Serial>) -> Unit){
        GlobalScope.launch(Dispatchers.IO) {
            val result = arrayListOf<Serial>()
            val subscriptions = db.dao().getSubscriptions()
            for(subscription in subscriptions){
                val serial = db.dao().getSerial(subscription.content_id)
                result.add(serial)
            }
            callback(result)
        }
    }
    fun removeNotification(notification: Notification){
        GlobalScope.launch {
            val result = db.dao().deleteNotification(notification)
        }
    }
    fun removeSubscription(id: Int){
        GlobalScope.launch {
            val result = db.dao().deleteSubscription(id)
        }
    }
    fun subscriptionIf(id: Int){
        GlobalScope.launch {
            val isExisting = db.dao().getSubscriptionById(id)
            if(isExisting == null){
                val serial = db.dao().getSerial(id)
                val count = allCountEpisodeSerialSync(id)
                if(count != null){
                    db.dao().addSubscription(Subscription().apply {
                        this.count = count
                        this.content_id = serial.id
                    })
                }
            }else{
                db.dao().deleteSubscription(id)
            }
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
    fun htmlPrompt(findString: String, data: String): String {
        return data.replace(findString, "<b>$findString</b>")
    }

    private fun getSecondTime(): Int{
        return (Date().time/1000).toInt()
    }

}