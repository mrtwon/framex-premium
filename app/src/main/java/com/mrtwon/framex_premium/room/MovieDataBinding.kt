package com.mrtwon.framex_premium.room

import com.mrtwon.framex_premium.ContentResponse.Movie
import com.mrtwon.framex_premium.Helper.HelperFunction.Companion.roundRating
import com.mrtwon.framex_premium.retrofit.framexPojo.responseMovie.GenresItem
import java.lang.StringBuilder

class MovieDataBinding(val movie: Movie){
    val NO_DATA: String = "Нет данных."
    val NO_DESCRIPTION: String = "Описание отсутствует"
    var genres: String = getGenres(movie.genres)
    var ru_title: String = movie.ru_title ?: NO_DATA
        get() = if(movie.year != null) "$field  (${movie.year})" else field
    var description: String = movie.description ?: NO_DESCRIPTION
    var kp_rating: String = roundRating(movie.kinopoisk_raintg?.toDouble())
    var imdb_rating: String = roundRating(movie.imdb_rating?.toDouble())



    //helper function
    fun getGenres(genresList: List<GenresItem?>?): String{
        if(genresList == null || genresList.isEmpty()) return "Нет жанра."
        val result = StringBuilder()
        result.append("(")
        for(key in genresList.indices){
            if(key >= 4){
                result.append("...")
                break
            }
            if(key == genresList.size-1) result.append(genresList[key]?.genre ?: continue)
            else result.append("${genresList[key]?.genre ?: continue},")
        }
        result.append(")")
        return result.toString()
    }
}