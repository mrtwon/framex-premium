package com.mrtwon.framex_premium.Content

import androidx.annotation.DrawableRes
import com.mrtwon.framex_premium.R

enum class GenresEnum(@DrawableRes val image: Int) {
    COMEDY(R.drawable.comedy){ override fun toString(): String = "комедия" },
    HORROR(R.drawable.ujasi){ override fun toString(): String = "ужасы"},
    ADVENTURE(R.drawable.priklyhenia){ override fun toString(): String = "приключения"},
    DETECTIVE(R.drawable.detective){ override fun toString(): String = "детектив"},
    ACTION(R.drawable.boevik){ override fun toString(): String = "боевик"},
    DRAMA(R.drawable.darma){ override fun toString(): String = "драма"},
    DOCUMENTARYFILM(R.drawable.documentalks){ override fun toString(): String = "документальный"},
    BIOGRAPHY(R.drawable.biogriphik){ override fun toString(): String = "биография"},
    FANTASY(R.drawable.fanstasy){ override fun toString(): String = "фэнтези"},
    CRIMINAL(R.drawable.criminal){ override fun toString(): String = "криминал" }
}