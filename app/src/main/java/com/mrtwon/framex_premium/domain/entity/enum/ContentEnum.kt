package com.mrtwon.framex_premium.domain.entity.enum

enum class ContentEnum(val _name: String, val type: String) {
    Movie("Фильм", "movie"),
    Serial("Сериал", "tv_series"),
    Undefined("undefined", "undefined");

    companion object{
        fun getEnum(string: String?): ContentEnum{
            for (elem in ContentEnum.values()){
                if(elem.type == string) return elem
            }
            return Undefined
        }
    }
}