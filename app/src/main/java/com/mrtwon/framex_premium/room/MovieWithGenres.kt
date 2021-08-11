package com.mrtwon.framex_premium.room

import androidx.room.Entity

@Entity
open class MovieWithGenres: Movie() {
    open var genres: String? = null
}