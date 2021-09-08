package com.mrtwon.framex_premium.Content

import androidx.annotation.DrawableRes
import com.google.rpc.context.AttributeContext
import com.mrtwon.framex_premium.MyApplication
import com.mrtwon.framex_premium.R

enum class GenresEnum(@DrawableRes val image: Int) {
    COMEDY(R.drawable.comedy){ override fun toString(): String = MyApplication.getInstance.getString(R.string.comedy_type)},
    HORROR(R.drawable.ujasi){ override fun toString(): String = MyApplication.getInstance.getString(R.string.horror_type)},
    ADVENTURE(R.drawable.priklyhenia){ override fun toString(): String = MyApplication.getInstance.getString(R.string.adventure_type)},
    DETECTIVE(R.drawable.detective){ override fun toString(): String = MyApplication.getInstance.getString(R.string.detective_type)},
    ACTION(R.drawable.boevik){ override fun toString(): String = MyApplication.getInstance.getString(R.string.action_type)},
    DRAMA(R.drawable.darma){ override fun toString(): String = MyApplication.getInstance.getString(R.string.drama_type)},
    DOCUMENTARYFILM(R.drawable.documentalks){ override fun toString(): String = MyApplication.getInstance.getString(R.string.documental_type)},
    BIOGRAPHY(R.drawable.biogriphik){ override fun toString(): String = MyApplication.getInstance.getString(R.string.biography_type)},
    FANTASY(R.drawable.fanstasy){ override fun toString(): String = MyApplication.getInstance.getString(R.string.fantasy_type)},
    CRIMINAL(R.drawable.criminal){ override fun toString(): String = MyApplication.getInstance.getString(R.string.criminal_type)}
}