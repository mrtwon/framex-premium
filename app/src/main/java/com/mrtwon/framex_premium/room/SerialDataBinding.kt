package com.mrtwon.framex_premium.room

import com.mrtwon.framex_premium.ContentResponse.Serial
import com.mrtwon.framex_premium.Helper.HelperFunction
import com.mrtwon.framex_premium.retrofit.framexPojo.responseSerial.GenresItem
import java.lang.StringBuilder

class SerialDataBinding(val serial: Serial){
    val NO_DATA: String = "Нет данных."
    var genres: String = getGenres(serial.genres)
    var ru_title: String = serial.ru_title ?: NO_DATA
        get() = if(serial.year != null) "$field  (${serial.year})" else field
    var description: String = serial.description ?: NO_DATA
    var kp_rating: String = HelperFunction.roundRating(serial.kinopoisk_raintg?.toDouble())
    var imdb_rating: String = HelperFunction.roundRating(serial.imdb_rating?.toDouble())


    //helper function
    private fun getGenres(genresList: List<GenresItem?>?): String{
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