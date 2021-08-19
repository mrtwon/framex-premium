package com.mrtwon.framex_premium.retrofit.KinopoiskRating


class RatingPOJO{
    var kp: Double? = null
    var imdb: Double? = null
    override fun toString(): String {
        return "kp $kp | imdb $imdb"
    }
}