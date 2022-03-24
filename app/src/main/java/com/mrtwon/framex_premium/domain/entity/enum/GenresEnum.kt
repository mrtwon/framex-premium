package com.mrtwon.framex_premium.domain.entity.enum

import com.mrtwon.framex_premium.R


enum class GenresEnum(val image: Int, val _name: String) {
    Comedy(R.drawable.comedy, "Комедия"),
    Horror(R.drawable.ujasi, "Ужасы"),
    Adventure(R.drawable.priklyhenia, "Приключения"),
    Detective(R.drawable.detective, "Детектив"),
    Action(R.drawable.boevik, "Боевик"),
    Drama(R.drawable.drama, "Драма"),
    DocFilm(R.drawable.documentalks, "Документальный"),
    Biography(R.drawable.biogriphik, "Биография"),
    Fantasy(R.drawable.fanstasy, "Фэнтези"),
    Criminal(R.drawable.criminal, "Криминал"),
    None(0, "None");
    companion object{
        fun getEnum(string: String?): GenresEnum{
            for(elem in values()){
                if(elem._name == string) return elem
            }
            return None
        }
    }
}