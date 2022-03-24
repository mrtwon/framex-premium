package com.mrtwon.framex_premium.domain.entity

data class Notification(
    var id: Int = 0,
    var idContent: Int,
    var season: String,
    var episode: String,
    var ruTitle: String? = null
)