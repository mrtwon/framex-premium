package com.mrtwon.framex_premium.FragmentTop

import com.mrtwon.framex_premium.Content.GenresEnum
import com.mrtwon.framex_premium.Content.RatingEnum

data class Filter(var rating: RatingEnum, var genres: GenresEnum?){
    override fun equals(other: Any?): Boolean {
        val obj = other as Filter
        return obj.rating.toString() == rating.toString() && obj.genres.toString() == genres.toString()
    }
    override fun hashCode(): Int {
        var result = rating.hashCode()
        result = 31 * result + (genres?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Rating $rating, Genres $genres"
    }
}