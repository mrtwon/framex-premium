package com.mrtwon.framex_premium.data.networkDataSource.firebaseSource

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.ui.text.toLowerCase
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.*
import com.mrtwon.framex_premium.data.entity.ContentEntity
import com.mrtwon.framex_premium.data.extenstion.toContent
import com.mrtwon.framex_premium.data.extenstion.toContentItemPage
import com.mrtwon.framex_premium.domain.entity.ContentItemPage
import com.mrtwon.framex_premium.domain.entity.enum.ContentEnum
import com.mrtwon.framex_premium.domain.entity.enum.GenresEnum
import com.mrtwon.framex_premium.domain.entity.enum.RatingEnum
import com.mrtwon.framex_premium.domain.entity.enum.StartPosition
import com.mrtwon.framex_premium.domain.usecase.GetContentByDescriptionPaging
import com.mrtwon.framex_premium.domain.usecase.GetContentByTitlePaging
import com.mrtwon.framex_premium.domain.usecase.GetTopContentByGenresPaging
import com.mrtwon.framex_premium.domain.usecase.GetTopContentByYearPaging
import java.util.*
import javax.inject.Inject

class FirebaseDataSource @Inject constructor(val firestore: FirebaseFirestore, val sharedPreference: SharedPreferences) {
    private val contentByIdInterval = 3600000 * 8
    private val topInterval = 3600000 * 8
    private val searchInterval = 3600000 * 2
    fun getContentById(id: Int, content: ContentEnum): () -> Task<QuerySnapshot>{
        val query = firestore.collection("Content")
            .whereEqualTo("contentType", content.type)
            .whereEqualTo("id", id)
        val hash = sumHash(id.hashCode(), content.hashCode())
        return {
            afterCompletion(query, getFromCache(hash, contentByIdInterval))
        }
    }

    fun getTopByYear(param: GetTopContentByYearPaging.Params): () -> WrapperResponse {
        val year = param.primaryParam.year //
        val rating = param.primaryParam.sortBy //
        val genres = param.primaryParam.genresEnum //
        val itemKey = param.itemKey
        val position = param.position
        val limit = param.limit
        val contentType = param.primaryParam.content.type //
        val hash = sumHash(year.hashCode(), rating.hashCode(), genres.hashCode(), contentType.hashCode())
        var query = firestore.collection("Content")
            .whereEqualTo("contentType", contentType)
            .whereEqualTo("year", year)
        if(genres != GenresEnum.None){
            query = query.whereArrayContains("genresList", genres._name.toLowerCase(Locale.ROOT))
        }

        convertRatingToDir(rating)?.let { query = query.orderBy(it, Query.Direction.DESCENDING) }

        if(itemKey != null){
           query =  when(position){
                StartPosition.After -> query.startAfter((itemKey as ContentItemPage).itemKey as DocumentSnapshot)
                StartPosition.Before -> query.endBefore((itemKey as ContentItemPage).itemKey as DocumentSnapshot)
            }
        }

        query = query.limit(limit)
        return {
            if(itemKey == null){
                firstAfterCompletion(query = query, hash = hash, interval = 1000*15)
            }else{
                val fromCache = (itemKey as ContentItemPage).fromCache
                secondAfterCompletion(query = query, fromCache = fromCache)
            }
        }
        //return WrapperResponse(afterCompletion(query, isFromCache), isFromCache)
    }
    fun getTopByGenres(param: GetTopContentByGenresPaging.Params)
        : () -> WrapperResponse{
        val contentType = param.primaryParam.content.type
        val genres = param.primaryParam.genres._name.toLowerCase(Locale.ROOT)
        val rating = param.primaryParam.sortBy
        val position = param.position
        val itemKey = param.itemKey
        val limit = param.limit
        val hash = sumHash(contentType.hashCode(), genres.hashCode(), rating.hashCode())

        var query = firestore.collection("Content")
            .whereEqualTo("contentType", contentType)
            .whereArrayContains("genresList", genres)
        convertRatingToDir(rating)?.let { query = query.orderBy(it, Query.Direction.DESCENDING) }

        if(itemKey != null){
            query = when(position){
                StartPosition.After -> query.startAfter(((itemKey as ContentItemPage).itemKey) as DocumentSnapshot)
                StartPosition.Before -> query.endBefore((itemKey as ContentItemPage).itemKey as DocumentSnapshot)
            }
        }

        query = query.limit(limit)
        return {
            if(itemKey == null){
                firstAfterCompletion(query = query, hash = hash, interval = 1000*15)
            }else{
                val fromCache = (itemKey as ContentItemPage).fromCache
                secondAfterCompletion(query = query, fromCache = fromCache)
            }
        }
    }

    fun getContentByDescription(param: GetContentByDescriptionPaging.Params): () -> WrapperResponse{
        val descList = lowerCaseArrayList(param.primaryParam.descriptionList)
        val rating = param.primaryParam.sortBy
        val itemKey = param.itemKey
        val position = param.position
        val limit = param.limit
        val hash = sumHash(descList.hashCode(), rating.hashCode())
        var query = firestore.collection("Content")
            .whereArrayContainsAny("descriptionList", descList)

        convertRatingToDir(rating)?.let {
            query = query.orderBy(it, Query.Direction.DESCENDING)
        }

        if(itemKey != null){
            query = when(position){
                StartPosition.After -> query.startAfter(((itemKey as ContentItemPage).itemKey) as DocumentSnapshot)
                StartPosition.Before -> query.endBefore((itemKey as ContentItemPage).itemKey as DocumentSnapshot)
            }
        }

        query = query.limit(limit)
        return {
            if(itemKey == null){
                firstAfterCompletion(query = query, hash = hash, interval = 1000*15)
            }else{
                val fromCache = (itemKey as ContentItemPage).fromCache
                secondAfterCompletion(query = query, fromCache = fromCache)
            }
        }
    }

    fun getContentByTitle(param: GetContentByTitlePaging.Params): () -> WrapperResponse{
        val title = param.primaryParam.title
        val rating = param.primaryParam.sortBy
        val position = param.position
        val itemKey = param.itemKey
        val limit = param.limit
        val hash = sumHash(title.hashCode(), rating.hashCode())
        var query = firestore.collection("Content")
            .whereArrayContains("ruTitleList", title.toLowerCase(Locale.ROOT))
        convertRatingToDir(rating)?.let {
            query = query.orderBy(it, Query.Direction.DESCENDING)
        }
        if(itemKey != null){
            query = when(position){
                StartPosition.After -> query.startAfter(((itemKey as ContentItemPage).itemKey) as DocumentSnapshot)
                StartPosition.Before -> query.endBefore((itemKey as ContentItemPage).itemKey as DocumentSnapshot)
            }
        }

        query = query.limit(limit)
        return {
            if(itemKey == null){
                firstAfterCompletion(query = query, hash = hash, interval = 1000*15)
            }else{
                val fromCache = (itemKey as ContentItemPage).fromCache
                secondAfterCompletion(query = query, fromCache = fromCache)
            }
        }
    }

    private fun firstAfterCompletion(query: Query, hash: Int, interval: Int): WrapperResponse{
        val timePassed = timePassed(hash)
        var task: Task<QuerySnapshot>
        var isFromCache: Boolean = false
        if(timePassed){
            Log.i("self-test-cache-control","switch server source")
            task = query.get(Source.SERVER)
            Tasks.await(task)
            if(task.isSuccessful && task.exception == null){
                Log.i("self-test-cache-control","server source successful")
                updateTime(hash = hash, interval = interval)
                isFromCache = false
            }else{
                Log.i("self-test-cache-control","server source not successful")
            }
        }else{
            Log.i("self-test-cache-control","switch cache source")
            task = query.get(Source.CACHE)
            Tasks.await(task)
            if(!task.isSuccessful && task.exception == null){
               Log.i("self-test-cache-control","cache source not successful")
               task = query.get()
               Tasks.await(task)
               isFromCache = false
            }else{
                Log.i("self-test-cache-control","cache source successful")
                isFromCache = true
            }
        }
        return WrapperResponse(task = task, fromCache = isFromCache)
    }
    private fun secondAfterCompletion(query: Query, fromCache: Boolean): WrapperResponse{
        return if(fromCache){
            var task = query.get(Source.CACHE)
            Tasks.await(task)
            if(!task.isSuccessful && task.exception == null){
                task = query.get()
                Tasks.await(task)
            }
            WrapperResponse(task = task, fromCache = fromCache)
        }else{
            val task = query.get(Source.SERVER)
            Tasks.await(task)
            WrapperResponse(task = task, fromCache = fromCache)
        }
    }
    private fun afterCompletion(query: Query, fromCache: Boolean): Task<QuerySnapshot>{
        return if(fromCache){
            var task = query.get(Source.CACHE)
            Tasks.await(task)
            if(!task.isSuccessful && task.exception == null){
                Log.i("self-retry","from Default")
                task = query.get()
                Tasks.await(task)
            }else{
                Log.i("self-retry","from Cache")
            }
            task
        }else{
            val task = query.get(Source.SERVER)
            Tasks.await(task)
            task
        }
    }
    private fun countCacheData(query: QuerySnapshot){
        var countFromCache = 0
        val total = query.documents.size
        query.documents.forEach {
            if(it.metadata.isFromCache){
                countFromCache +=1
            }
        }
        Log.i("self-debug-cache","all $total fromCache $countFromCache")
    }

    private fun convertRatingToDir(rating: RatingEnum): String?{
        return when(rating){
            RatingEnum.Kinopoisk -> "ratingMap.kinopoisk"
            RatingEnum.Imdb -> "ratingMap.imdb"
            RatingEnum.None -> null
        }
    }
    private fun lowerCaseArrayList(arr: List<String>): List<String>{
        val newList = arrayListOf<String>()
        arr.forEach {
            newList.add(it.toLowerCase(Locale.ROOT))
        }
        return newList
    }

    private fun getTime(hash: Int): Long{
        return sharedPreference.getLong(hash.toString(), 0)
    }
    private fun updateTime(hash: Int, interval: Int){
        sharedPreference.edit()
            .putLong(hash.toString(), Date().time + interval)
            .apply()
    }
    private fun timePassed(hash: Int): Boolean{
        return Date().time > sharedPreference.getLong(hash.toString(), 0)
    }
    private fun getFromCache(hash: Int, interval: Int): Boolean{
        val currentTime = Date().time
        val timeMs = sharedPreference.getLong(hash.toString(), 0)
        Log.i("self-debug-cache","current $currentTime db $timeMs interval $interval")
        if(timeMs == 0L){
            Log.i("self-debug-cache","== 0l")
            sharedPreference
                .edit()
                .putLong(hash.toString(), currentTime)
                .apply()
            return false
        }
        return if(currentTime > (timeMs + interval)){
            Log.i("self-debug-cache","${(timeMs+interval)} > $currentTime")

            sharedPreference
                .edit()
                .putLong(hash.toString(), currentTime)
                .apply()
            false
        }else{
            Log.i("self-debug-cache","${(timeMs+interval)} < $currentTime")
            true
        }
    }
    private fun sumHash(vararg hash: Int): Int{
        var result = 1
        hash.forEach { result += it }
        return result/10
    }
}