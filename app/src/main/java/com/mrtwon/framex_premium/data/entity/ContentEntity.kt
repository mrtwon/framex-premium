package com.mrtwon.framex_premium.data.entity

import com.mrtwon.framex_premium.domain.entity.enum.ContentEnum
import com.mrtwon.framex_premium.domain.entity.Content

class ContentEntity {
    var id: Int = 0
    var kpId: String? = null
    var imdbId: String? = null
    var ruTitleList: List<String> = arrayListOf()
    var descriptionList: List<String> = arrayListOf()
    var ratingMap: Map<String, Double> = mapOf()
    var genresList: List<String> = arrayListOf()
    var ruTitle: String? = null
    var description: String? = null
    var genres: String? = null
    var year: Int? = null
    var contentType: String? = null
    var poster: String? = null
    var posterPreview: String? = null
    var iframe_src: String? = null

    fun toContent(): Content {
        return Content(
            id = id,
            kpId = kpId,
            imdbId = imdbId,
            ruTitle = ruTitle,
            description = description,
            year = year,
            contentType = ContentEnum.getEnum(contentType),
            genres = genres,
            poster = poster,
            posterPreview = posterPreview,
            kpRating = ratingMap.get("kinopoisk"),
            imdbRating = ratingMap.get("imdb"),
            iframeSrc = iframe_src
        )
    }
}