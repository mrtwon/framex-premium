package com.mrtwon.framex_premium.Model

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.LiveData
import com.example.testbook.Retrofit.Kinopoisk.KinopoiskApi
import com.github.mrtwon.library.XmlParse
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.google.gson.GsonBuilder
import com.mrtwon.framex_premium.Content.CollectionContentEnum
import com.mrtwon.framex_premium.room.Database
import com.mrtwon.framex_premium.Content.ContentTypeEnum
import com.mrtwon.framex_premium.Content.GenresEnum
import com.mrtwon.framex_premium.Content.RatingEnum
import com.mrtwon.framex_premium.ContentResponse.ContentResponse
import com.mrtwon.framex_premium.ContentResponse.Movie
import com.mrtwon.framex_premium.ContentResponse.Serial
import com.mrtwon.framex_premium.FragmentTop.Filter
import com.mrtwon.framex_premium.Helper.DetailsError
import com.mrtwon.framex_premium.Helper.TYPE_ERROR
import com.mrtwon.framex_premium.retrofit.framexPojo.FramexApi
import com.mrtwon.framex_premium.retrofit.VideoCdn.VideoCdnApi
import com.mrtwon.framex_premium.retrofit.framexAuth.Response
import com.mrtwon.framex_premium.retrofit.framexAuth.ResponseError
import com.mrtwon.framex_premium.retrofit.framexAuth.ResponseUser
import com.mrtwon.framex_premium.room.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import java.lang.Exception
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/*
* Model Class
* Working with network and database
 */
class Model(val db: Database, private val kinopoiskApi: KinopoiskApi, private val videoCdnApi: VideoCdnApi, private val ratingApi: XmlParse, private val fxApi: FramexApi) {
    val auth = FirebaseAuth.getInstance()
    val storage = Firebase.storage
    /*
    Model Api Fx Auth
     */

    fun isAuth(callbackIsAuth: (Boolean) -> Unit, callbackError: (DetailsError) -> Unit){
        try {
            callbackIsAuth(auth.currentUser != null)
        }catch (e: Exception){
            callbackError(ERR_UNKNOWN)
        }
    }
    fun logout(callbackError: (DetailsError) -> Unit){
        try{
            auth.signOut()
        }catch (e: Exception){
            val returnError = when(e){
                is SocketTimeoutException -> ERR_TIMEOUT_FATAL
                is ConnectException -> ERR_NO_CONNECT_FATAL
                else -> ERR_UNKNOWN
            }
            callbackError(returnError)
        }
    }

    suspend fun giveMeUserProfile(
        callbackError: (DetailsError) -> Unit,
        callbackProfile: (ResponseUser) -> Unit
    ) {
        giveIdToken(callbackError)?.let { token ->
            try {
                fxApi.giveMeProfile(token).execute().let { response ->
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            callbackProfile(response.body()!!)
                        } else {
                            callbackError(ERR_NO_READY_ANSWER)
                        }
                    } else {
                        processingException(
                            response.errorBody()?.string(),
                            callbackError,
                            response.code()
                        )
                    }
                }
            }catch (e: Exception){
                val returnError = when(e){
                    is SocketTimeoutException -> ERR_TIMEOUT_FATAL
                    is ConnectException -> ERR_NO_CONNECT_FATAL
                    else -> ERR_UNKNOWN
                }
                e.printStackTrace()
                callbackError(returnError)
            }
        }
    }

    suspend fun createPhoto(photo: Uri, callbackError: (DetailsError) -> Unit, callbackConfirm: (Boolean) -> Unit){
        try{
            if(!pingFxServer(callbackError)) return
            uploadPhoto(photo, callbackError)?.let { photoUrl ->
                giveIdToken(callbackError)?.let {  token ->
                    fxApi.createPhoto(token, photoUrl).execute().let { response ->
                        if(response.isSuccessful){
                            callbackConfirm(true)
                        }else{
                            processingException(
                                response.errorBody()?.string(),
                                callbackError,
                                response.code()
                            )
                        }
                    }
                }
            }

        }catch (e: Exception){
            val returnError = when(e){
                is SocketTimeoutException -> ERR_TIMEOUT_FATAL
                is ConnectException -> ERR_NO_CONNECT_FATAL
                else -> ERR_UNKNOWN
            }
            callbackError(returnError)
        }
    }
        suspend fun createNickName(
            nickName: String,
            callbackError: (DetailsError) -> Unit,
            callbackConfirm: (Boolean) -> Unit
        ) {
            try {
                giveIdToken(callbackError)?.let { token ->
                    fxApi.createNickName(token, nickName).execute().let { response ->
                        if (response.isSuccessful) {
                            callbackConfirm(true)
                        } else {
                            processingException(
                                response.errorBody()?.string(),
                                callbackError,
                                response.code()
                            )
                        }
                    }
                }
            }catch (e: Exception){
                val returnError = when(e){
                    is SocketTimeoutException -> ERR_TIMEOUT_FATAL
                    is ConnectException -> ERR_NO_CONNECT_FATAL
                    else -> ERR_UNKNOWN
                }
                callbackError(returnError)
            }
        }

        suspend fun sendCreateUserRequest(
            email: String,
            password: String,
            callbackError: (DetailsError) -> Unit,
            callbackConfirm: (Boolean) -> Unit
        ) {
            try {
                if (!pingFxServer(callbackError)) return
                if (registrationByEmailPassword(email, password, callbackError)) {
                    giveIdToken(callbackError)?.let { token ->
                        fxApi.sendCreateUserRequest(token).execute().let { response ->
                            if (response.isSuccessful) {
                                callbackConfirm(true)
                            } else {
                                processingException(
                                    response.errorBody()?.string(),
                                    callbackError,
                                    response.code()
                                )
                            }
                        }
                    }
                }
            }catch (e: Exception){
                val returnError = when(e){
                    is SocketTimeoutException -> ERR_TIMEOUT_FATAL
                    is ConnectException -> ERR_NO_CONNECT_FATAL
                    else -> ERR_UNKNOWN
                }
                callbackError(returnError)
            }
        }

        suspend fun createAboutMe(about: String, callbackError: (DetailsError) -> Unit, callbackConfirm: (Boolean) -> Unit) {
            try {
                giveIdToken(callbackError)?.let { token ->
                    fxApi.createAboutMe(token, about).execute().let { response ->
                        if (response.isSuccessful) {
                            callbackConfirm(true)
                        } else {
                            processingException(
                                response.errorBody()?.string(),
                                callbackError,
                                response.code()
                            )
                        }
                    }
                }
            }catch (e: Exception){
                val returnError = when(e){
                    is SocketTimeoutException -> ERR_TIMEOUT_FATAL
                    is ConnectException -> ERR_NO_CONNECT_FATAL
                    else -> ERR_UNKNOWN
                }
                callbackError(returnError)
            }
        }

        suspend fun createFavoriteContent(favorite: String, callbackError: (DetailsError) -> Unit, callbackConfirm: (Boolean) -> Unit) {
            try {
                giveIdToken(callbackError)?.let { token ->
                    fxApi.createFavorite(token, favorite).execute().let { response ->
                        if (response.isSuccessful) {
                            callbackConfirm(true)
                        } else {
                            processingException(
                                response.errorBody()?.string(),
                                callbackError,
                                response.code()
                            )
                        }
                    }
                }
            }catch (e: Exception){
                val returnError = when(e){
                    is SocketTimeoutException -> ERR_TIMEOUT_FATAL
                    is ConnectException -> ERR_NO_CONNECT_FATAL
                    else -> ERR_UNKNOWN
                }
                callbackError(returnError)
            }
        }

        suspend fun createOpenFavorite(isOpen: Boolean, callbackError: (DetailsError) -> Unit, callbackConfirm: (Boolean) -> Unit) {
            try {
                giveIdToken(callbackError)?.let { token ->
                    val isOpenString = isOpen.toString().toLowerCase(Locale.ROOT)
                    log("value is $isOpenString")
                    fxApi.createOpenFavorite(token, isOpenString).execute().let { response ->
                        if (response.isSuccessful) {
                            callbackConfirm(true)
                        } else {
                            processingException(
                                response.errorBody()?.string(),
                                callbackError,
                                response.code()
                            )
                        }
                    }
                }
            }catch (e: Exception) {
                val returnError = when(e){
                    is SocketTimeoutException -> ERR_TIMEOUT_FATAL
                    is ConnectException -> ERR_NO_CONNECT_FATAL
                    else -> ERR_UNKNOWN
                }
                callbackError(returnError)
            }
        }


        fun login(email: String, password: String, callbackError: (DetailsError) -> Unit, callbackConfirm: (Boolean) -> Unit){
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    callbackConfirm(true)
                }else{
                    callbackError(DetailsError.fromException(exception = task.exception))
                }
            }
        }

        suspend fun pingFxServer(callbackError: (DetailsError) -> Unit) = suspendCoroutine<Boolean> {
            fxApi.pingFxServer().enqueue(object: Callback<Unit>{
                override fun onResponse(call: Call<Unit>, response: retrofit2.Response<Unit>) {
                    it.resume(true)
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    callbackError(ERR_TIMEOUT)
                    it.resume(false)
                }

            })
        }

        /*
    Model Api Fx
     */


        fun getContentResponse(id: Int, contentType: String, callback: (ContentResponse?) -> Unit) {
            GlobalScope.launch {
                when (contentType) {
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
        fun searchContentByTitle(
            query: String,
            pageMovie: Int?,
            pageSerial: Int?,
            callbackSerial: (List<ContentResponse>) -> Unit,
            callbackMovie: (List<ContentResponse>) -> Unit,
            callbackAll: (List<ContentResponse>) -> Unit,
            callbackConnectError: (Boolean) -> Unit
        ) {
            GlobalScope.launch(CoroutineExceptionHandler { context, error ->
                error.printStackTrace()
                callbackConnectError(true)
            }) {

                val empty = arrayListOf<ContentResponse>()
                val result = arrayListOf<ContentResponse>()

                pageMovie?.let {
                    val responseMovie = fxApi.searchMovieByTitle(query, it).execute()
                    if (responseMovie.code() == 404) callbackMovie(empty)
                    else {
                        val mResult = Movie.buildMovies(responseMovie.body())
                        callbackMovie(mResult)
                        result.addAll(mResult)
                    }
                }

                pageSerial?.let {
                    val responseSerial = fxApi.searchSerialByTitle(query, it).execute()
                    if (responseSerial.code() == 404) callbackSerial(empty)
                    else {
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
        fun searchContentByDescription(
            query: String,
            pageMovie: Int?,
            pageSerial: Int?,
            callbackMovie: (List<ContentResponse>) -> Unit,
            callbackSerial: (List<ContentResponse>) -> Unit,
            callbackAll: (List<ContentResponse>) -> Unit,
            callbackConnectError: (Boolean) -> Unit
        ) {
            GlobalScope.launch(CoroutineExceptionHandler { context, error ->
                callbackConnectError(true)
                error.printStackTrace()
            }) {

                log("input model, lastMovie: ${pageMovie}, lastSerial ${pageSerial}")

                val empty = arrayListOf<ContentResponse>()
                val result = arrayListOf<ContentResponse>()

                pageMovie?.let {
                    val responseMovie = fxApi.searchMovieByDescription(query, it).execute()
                    if (responseMovie.code() == 404) callbackMovie(empty)
                    else {
                        val mResult = Movie.buildMovies(responseMovie.body())
                        callbackMovie(mResult)
                        result.addAll(mResult)
                    }
                }

                pageSerial?.let {
                    val responseSerial = fxApi.searchSerialByDescription(query, it).execute()
                    if (responseSerial.code() == 404) callbackSerial(empty)
                    else {
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
        fun getAboutMovie(id: Int, callback: (Movie?) -> Unit, callbackError: (Boolean) -> Unit) {
            GlobalScope.launch(CoroutineExceptionHandler { context, error ->
                callbackError(true)
            }) {
                val response = fxApi.getAboutMovie(id).execute().body()
                val result = Movie.buildMovie(response)
                callback(result)
            }
        }

        @DelicateCoroutinesApi
        fun getAboutSerial(id: Int, callback: (Serial?) -> Unit, callbackError: (Boolean) -> Unit) {
            GlobalScope.launch(CoroutineExceptionHandler { context, exception ->
                callbackError(true)
            }) {
                val response = fxApi.getAboutSerial(id).execute().body()
                callback(Serial.buildSerial(response))
            }
        }


        fun getTopByCollectionEnum(
            collectionContentEnum: CollectionContentEnum,
            content: ContentTypeEnum,
            filter: Filter,
            page: Int,
            callback: (List<ContentResponse>) -> Unit,
            callbackConnectError: (Boolean) -> Unit
        ) {
            GlobalScope.launch(CoroutineExceptionHandler { context, exception ->
                callbackConnectError(true)
            }) {
                val orderingRating = convertRatingFilterToOrdering(filter)
                val genresString = filter.genres?.toString()?.toLowerCase(Locale.ROOT)
                when (content) {
                    ContentTypeEnum.MOVIE -> {
                        log("switch movie")
                        when (collectionContentEnum) {
                            CollectionContentEnum.NEW -> {
                                val response = if (filter.genres != null) {
                                    fxApi.getTopMovieByYearWithGenres(
                                        getYear(),
                                        page,
                                        orderingRating,
                                        genresString!!
                                    ).execute()
                                } else {
                                    fxApi.getTopMovieByYear(getYear(), page, orderingRating)
                                        .execute()
                                }
                                if (response.code() == 404) callback(arrayListOf())
                                else if (response.code() in 200..299) {
                                    callback(Movie.buildMovies(response.body()))
                                }
                            }
                        }
                    }
                    ContentTypeEnum.SERIAL -> {
                        log("switch serial")
                        when (collectionContentEnum) {
                            CollectionContentEnum.NEW -> {
                                val response = if (filter.genres != null) {
                                    fxApi.getTopSerialByYearWithGenres(
                                        getYear(),
                                        page,
                                        orderingRating,
                                        genresString!!
                                    ).execute()
                                } else {
                                    fxApi.getTopSerialByYear(getYear(), page, orderingRating)
                                        .execute()
                                }
                                if (response.code() == 404) callback(arrayListOf())
                                else if (response.code() in 200..299) {
                                    callback(Serial.buildSerials(response.body()))
                                }
                            }
                        }
                    }
                }
            }

        }

        @DelicateCoroutinesApi
        fun getTopByGenresEnum(
            genres: GenresEnum,
            content: ContentTypeEnum,
            filter: Filter,
            page: Int,
            callbackConnectError: (Boolean) -> Unit,
            callback: (List<ContentResponse>) -> Unit
        ) {
            GlobalScope.launch(CoroutineExceptionHandler { context, exception ->
                callbackConnectError(true)
            }) {
                val genresString = genres.toString().toLowerCase(Locale.ROOT)
                val orderingRating = convertRatingFilterToOrdering(filter)
                when (content) {
                    ContentTypeEnum.SERIAL -> {
                        val response =
                            fxApi.getTopSerialByGenres(genresString, page, orderingRating).execute()
                        if (response.code() == 404) {
                            callback(arrayListOf())
                        } else if (response.code() in 200..299) {
                            callback(Serial.buildSerials(response.body()))
                        }
                    }
                    ContentTypeEnum.MOVIE -> {
                        val response =
                            fxApi.getTopMovieByGenres(genresString, page, orderingRating).execute()
                        if (response.code() == 404) {
                            callback(arrayListOf())
                        } else if (response.code() in 200..299) {
                            callback(Movie.buildMovies(response.body()))
                        }
                    }
                }
            }
        }


        //helper function

        //log
        private fun log(s: String) {
            Log.i("self-model", s)
        }


        fun checkedBlockSync(id: Int, contentType: String): Boolean {
            when (contentType) {
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


        fun favoriteAction(content: ContentResponse) {
            GlobalScope.launch {
                val isFavorite =
                    db.dao().getFavoriteElement(content.id, content.contentType) != null
                if (isFavorite) {
                    db.dao().deleteFavoriteElement(content.id, content.contentType)
                } else {
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
                    if (isRecent(contentResponse.id, contentResponse.contentType)) {
                        val recent = db.dao()
                            .getRecentElement(contentResponse.id, contentResponse.contentType)
                        if (recent != null) {
                            recent.time = getSecondTime()
                            db.dao().updateRecent(recent)
                        }
                    } else {
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

        fun isRecent(id: Int, contentType: String): Boolean {
            return db.dao().getRecentElement(id, contentType) != null
        }

        fun getRecentList(callback: (List<Recent>) -> Unit) {
            GlobalScope.launch {
                val recentList = db.dao().getRecentList()
                callback(recentList)
            }
        }


        fun getNotificationList(callback: (List<Notification>) -> Unit) {
            GlobalScope.launch(Dispatchers.IO) {
                val result = db.dao().getNotification()
                callback(result)
            }
        }

        fun getNotificationListLiveData(): LiveData<List<Notification>> =
            db.dao().getNotificationLiveData()

        fun getSubscriptionListLiveData(): LiveData<List<Subscription>> =
            db.dao().getSubscriptionsLiveData()


        fun removeNotification(notification: Notification) {
            GlobalScope.launch {
                val result = db.dao().deleteNotification(notification)
            }
        }

        fun removeSubscription(subscription: Subscription) {
            GlobalScope.launch {
                db.dao().deleteSubscription(subscription)
            }
        }

        fun subscriptionAction(contentResponse: ContentResponse) {
            GlobalScope.launch {
                val isExisting = db.dao().getSubscriptionById(contentResponse.id)
                if (isExisting == null) {
                    val count = allCountEpisodeSerialSync(contentResponse.id)
                    if (count != null) {
                        val subscription = Subscription().apply {
                            this.count = count
                            this.content_id = contentResponse.id
                            this.poster = contentResponse.poster
                        }
                        log(subscription.toString())
                        db.dao().addSubscription(subscription)
                    }
                } else {
                    db.dao().deleteSubscriptionById(contentResponse.id)
                }
            }
        }

        fun addSubscription(subscription: Subscription) {
            GlobalScope.launch {
                db.dao().addSubscription(subscription)
            }
        }

        fun getSubscriptionByIdLiveData(id: Int): LiveData<Subscription> =
            db.dao().getSubscriptionByIdLiveData(id)

        fun allCountEpisodeSerialSync(id: Int): Int? {
            val response = videoCdnApi.serialById(id).execute().body()
            return response?.data?.get(0)?.episodeCount
        }


        /*
    Helper Functions
     */

        fun ParseHtmlPrompt(
            contents: List<ContentResponse>,
            findString: String
        ): List<ContentResponse> {
            val result = arrayListOf<ContentResponse>()
            for (element in contents) {
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

        private fun getSecondTime(): Int {
            return (Date().time / 1000).toInt()
        }

        private fun convertRatingFilterToOrdering(filter: Filter): String {
            return if (filter.rating == RatingEnum.Kinopoisk) "rating.kinopoisk" else "rating.imdb"
        }

        private fun getYear(): Int {
            val calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
            calendar.time = Date()
            return calendar.get(Calendar.YEAR)
        }

        private fun getTypeError(code: Int): TYPE_ERROR {
            return when (code) {
                401 -> TYPE_ERROR.FALIED
                in 500..599 -> TYPE_ERROR.FALIED
                else -> TYPE_ERROR.ERROR
            }
        }
        private suspend fun uploadPhoto(uri: Uri, callbackError: (DetailsError) -> Unit): String? = suspendCoroutine { coroutine ->
            val reference = storage.reference
            val uid = auth.currentUser?.uid
            if(uid == null){
                callbackError(ERR_NOT_AUTH)
                coroutine.resume(null)
            }

            val mountainsRef = reference.child(uid.toString())
            val mountainsImageRef = mountainsRef.child("avatar/${uid.toString()}")
            val uploadTask = mountainsImageRef.putFile(uri)

            uploadTask.continueWithTask { task ->
                if(!task.isSuccessful){
                    coroutine.resume(null)
                    callbackError(DetailsError.fromException(exception = task.exception))
                }
                mountainsImageRef.downloadUrl
            }.addOnCompleteListener {
                if(it.isSuccessful){
                    coroutine.resume(it.result.toString())
                }else{
                    log("error")
                    it.exception?.printStackTrace()
                }
            }
        }

    /*
    private void uploadFile() {
    if (mImageUri != null) {
        StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                + "." + getFileExtension(mImageUri));

        fileReference.putFile(mImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                //change made here
                return fileReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                        System.out.println("Upload success: " + downloadUri);
                } else {
                    // Handle failures
                    // ...
                }
            }
        });

    } else {
        Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
    }

}
     */


        private suspend fun registrationByEmailPassword(email: String, password: String, callbackError: (DetailsError) -> Unit): Boolean = suspendCoroutine {
               auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                   if(task.isSuccessful){
                       it.resume(true)
                   }else{
                       callbackError(DetailsError.fromException(exception = task.exception))
                       it.resume(false)
                   }
               }
        }
        suspend fun giveIdToken(callbackError: (DetailsError) -> Unit) = suspendCoroutine<String?> { coroutine ->
            var token: String? = null
            with(Dispatchers.IO) {
                if (auth.currentUser == null) {
                    callbackError(ERR_NOT_AUTH)
                    coroutine.resume(token)
                } else {
                    auth.currentUser!!.getIdToken(false).addOnCompleteListener { result ->
                        if (result.isSuccessful) {

                            if (result.result.token != null) {
                                token = result.result.token!!
                            } else {
                                callbackError(ERR_TOKEN)
                            }

                        } else {
                            callbackError(
                                DetailsError.fromException(
                                    TYPE_ERROR.FALIED,
                                    result.exception
                                )
                            )
                        }
                        coroutine.resume(token)
                    }
                }
            }
        }

        private fun processingException(responseString: String?, callbackError: (DetailsError) -> Unit, responseCode: Int? = null){
            if(responseString == null){
                log("is null")
                callbackError(ERR_NO_READY_ANSWER)
                return
            }
            val typeError: TYPE_ERROR = if(responseCode == null) TYPE_ERROR.ERROR else getTypeError(responseCode)
            val gson = GsonBuilder().create()
            try {
                val responseError: ResponseError = gson.fromJson(
                    responseString,
                    ResponseError::class.java
                )
                callbackError(
                    DetailsError.fromResponseError(
                        typeError,
                        responseError
                    )
                )
            } catch (e: Exception) {
                log("exception catch, response body: $responseString")
                callbackError(ERR_NO_READY_ANSWER)
            }
        }

        private val ERR_NOT_AUTH = DetailsError(TYPE_ERROR.FALIED, "Ошибка", "Не авторизован")
        private val ERR_TOKEN = DetailsError(TYPE_ERROR.FALIED, "Ошибка", "Ошибка токена")
        private val ERR_UNKNOWN = DetailsError(TYPE_ERROR.FALIED, "Ошибка", "Произошла незапланированная ошибка")
        private val ERR_NO_READY_ANSWER = DetailsError(TYPE_ERROR.FALIED, "Ошибка", "Некорректный ответ от сервера, произошла незапланированная ошибка")
        private val ERR_TIMEOUT = DetailsError(TYPE_ERROR.ERROR, "Timeout", "Время ожидания ответа истекло, попробуйте еще раз")
        private val ERR_TIMEOUT_FATAL = DetailsError(TYPE_ERROR.FALIED, "Timeout", "Время ожидания ответа истекло, попробуйте еще раз")
        private val ERR_NO_CONNECT_FATAL = DetailsError(TYPE_ERROR.FALIED, "Connect error", "Проверьте подключение к сети")
        private val ERR_NO_CONNECT = DetailsError(TYPE_ERROR.ERROR, "Connect error", "Проверьте подключение к сети")
        private fun errorOnClientSide(): DetailsError {
            return DetailsError(
                TYPE_ERROR.ERROR,
                "Ошибка",
                "Возникла ошибка на стороне клиента ..."
            )
        }

    }