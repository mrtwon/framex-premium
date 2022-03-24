package com.mrtwon.framex_premium.domain.entity

data class Subscription(
    var id: Int = 0,
    var contentId: Int,
    var posterPreview: String? = null,
    var ruTitle: String? = null,
    var episodeCount: Int = 0
)