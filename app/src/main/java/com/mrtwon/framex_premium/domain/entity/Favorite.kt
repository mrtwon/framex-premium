package com.mrtwon.framex_premium.domain.entity

import com.mrtwon.framex_premium.domain.entity.enum.ContentEnum

data class Favorite(
    var id: Int = 0,
    var idContent: Int,
    var ruTitle: String? = null,
    var contentType: ContentEnum,
    var posterPreview: String? = null
)