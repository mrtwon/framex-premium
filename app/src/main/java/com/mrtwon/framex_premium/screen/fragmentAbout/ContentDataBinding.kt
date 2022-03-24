package com.mrtwon.framex_premium.screen.fragmentAbout

import com.mrtwon.framex_premium.domain.entity.Content
import java.lang.StringBuilder

class ContentDataBinding(private val content: Content) {
    val ruTitle: String = connectYearToTitle(content.ruTitle)
    var description: String = content.description ?: "Нет данных"
    val genres: String = convertedGenres(content.genres)
    val kpRating: String = content.kpRating?.toString() ?: "0.0"
    val imdbRating: String = content.imdbRating?.toString() ?: "0.0"

    private fun connectYearToTitle(_ruTitle: String?): String{
        val ruTitleWithoutNull = _ruTitle ?: "Нет данных"
        return if(content.year != null){
            "$ruTitleWithoutNull(${content.year})"
        }else{
            ruTitleWithoutNull
        }
    }

    private fun convertedGenres(genres: String?): String{
        if(genres == null) return "(Нет данных)"
        var genresList = ArrayList<String>(genres.split(","))
        if(genresList.size >= 3){
            genresList = ArrayList(genresList.subList(0, 3))
            genresList.add("...")
        }
        val resultString = StringBuilder(genresList.toString())
        resultString[0] = '('
        resultString[resultString.lastIndex] = ')'
        return resultString.toString()
    }
}