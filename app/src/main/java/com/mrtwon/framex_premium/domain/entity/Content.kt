package com.mrtwon.framex_premium.domain.entity

import com.mrtwon.framex_premium.domain.entity.enum.ContentEnum

open class Content(
    val id: Int = 0,
    val kpId: String? = null,
    val imdbId: String? = null,
    val ruTitle: String? = null,
    var description: String? = null,
    var year: Int? = null,
    var contentType: ContentEnum = ContentEnum.Undefined,
    var genres: String? = null,
    var poster: String? = null,
    var posterPreview: String? = null,
    var kpRating: Double? = null,
    var imdbRating: Double? = null,
    var iframeSrc: String? = null
)