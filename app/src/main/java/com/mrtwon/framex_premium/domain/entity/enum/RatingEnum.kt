package com.mrtwon.framex_premium.domain.entity.enum

enum class RatingEnum(val _name: String) {
    Imdb("Imdb"),
    Kinopoisk("Kinopoisk"),
    None("None");
    companion object {
        fun getEnum(string: String): RatingEnum {
            for(elem in values())
                if(elem._name == string) return elem
            return None
        }
    }
}