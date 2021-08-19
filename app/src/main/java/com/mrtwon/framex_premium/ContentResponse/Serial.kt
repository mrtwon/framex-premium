package com.mrtwon.framex_premium.ContentResponse

import com.mrtwon.framex_premium.retrofit.testPOJO.responseMovie.ResponseMovie
import com.mrtwon.framex_premium.retrofit.testPOJO.responseSerial.CountriesItem
import com.mrtwon.framex_premium.retrofit.testPOJO.responseSerial.GenresItem
import com.mrtwon.framex_premium.retrofit.testPOJO.responseSerial.ResponseSerial
import com.mrtwon.framex_premium.retrofit.testPOJO.responseSerial.ResponseSerialItem

class Serial: ContentResponse(){
    //kinopoisk
    var premiereDvd: Any? = null
    var filmLength: String? = null
    val countries: List<CountriesItem?>? = null
    val type: String? = null
    var webUrl: String? = null
    var premiereRu: String? = null
    var genres: List<GenresItem?>? = null
    var premiereWorldCountry: String? = null
    var posterUrlPreview: String? = null
    var ratingMpaa: Any? = null
    var ratingAgeLimits: Int? = null

    //video cdn

    var endDate: Any? = null
    var episodeCount: Int? = null
    var contentId: Any? = null
    var previewIframeSrc: String? = null
    var created: String? = null
    var lastEpisodeId: Int? = null
    var blocked: Int? = null
    var updated: String? = null
    var seasonCount: Int? = null
    var countryId: Any? = null
    var startDate: String? = null

    companion object{
        fun buildSerial(responseSerial: ResponseSerial?): Serial?{
            if(responseSerial == null) return null
            val video_cdn = responseSerial.response?.get(0)?.videoCdn ?: return null
            val kinopoisk = responseSerial.response.get(0)?.kinopoisk ?: return null
            val rating = responseSerial.response.get(0)?.rating
            return Serial().apply {
                id = video_cdn.id
                imdb_id = video_cdn.imdbId
                kp_id = video_cdn.kinopoiskId
                ru_title = video_cdn.ruTitle
                description = kinopoisk.description
                orig_title = video_cdn.origTitle
                year = kinopoisk.year
                contentType = video_cdn.contentType
                poster = kinopoisk.posterUrl
                iframe_src = video_cdn.iframeSrc
                episodeCount = video_cdn.episodeCount
                lastEpisodeId = video_cdn.lastEpisodeId
                seasonCount = video_cdn.seasonCount
                endDate = video_cdn.endDate
                startDate = video_cdn.startDate


                poster = kinopoisk.posterUrl
                webUrl = kinopoisk.webUrl
                premiereRu = kinopoisk.premiereRu
                genres = kinopoisk.genres
                premiereWorldCountry = kinopoisk.premiereWorldCountry
                posterUrlPreview = kinopoisk.posterUrlPreview
                ratingMpaa = kinopoisk.ratingMpaa
                ratingAgeLimits = kinopoisk.ratingAgeLimits
                premiereDvd = kinopoisk.premiereDvd
                filmLength = kinopoisk.filmLength



                contentId = video_cdn.contentId
                previewIframeSrc = video_cdn.previewIframeSrc
                created = video_cdn.created
                iframe_src = video_cdn.iframeSrc
                blocked = video_cdn.blocked
                updated = video_cdn.updated

                countryId = video_cdn.contentId

                imdb_rating = rating?.imdb
                kinopoisk_raintg = rating?.kinopoisk
            }
        }


        fun buildSerials(responseSerial: ResponseSerial?): List<Serial>{
            val result = arrayListOf<Serial>()
            val size = responseSerial?.response?.size ?: return result
            for(i in 0 until size){
                val video_cdn = responseSerial.response.get(i)?.videoCdn ?: return result
                val kinopoisk = responseSerial.response.get(i)?.kinopoisk ?: return result
                val rating = responseSerial.response.get(i)?.rating
                result.add(Serial().apply {
                    id = video_cdn.id
                    imdb_id = video_cdn.imdbId
                    kp_id = video_cdn.kinopoiskId
                    ru_title = video_cdn.ruTitle
                    description = kinopoisk.description
                    orig_title = video_cdn.origTitle
                    year = kinopoisk.year
                    contentType = video_cdn.contentType
                    poster = kinopoisk.posterUrl
                    iframe_src = video_cdn.iframeSrc
                    episodeCount = video_cdn.episodeCount
                    lastEpisodeId = video_cdn.lastEpisodeId
                    seasonCount = video_cdn.seasonCount
                    endDate = video_cdn.endDate
                    startDate = video_cdn.startDate


                    poster = kinopoisk.posterUrl
                    webUrl = kinopoisk.webUrl
                    premiereRu = kinopoisk.premiereRu
                    genres = kinopoisk.genres
                    premiereWorldCountry = kinopoisk.premiereWorldCountry
                    posterUrlPreview = kinopoisk.posterUrlPreview
                    ratingMpaa = kinopoisk.ratingMpaa
                    ratingAgeLimits = kinopoisk.ratingAgeLimits
                    premiereDvd = kinopoisk.premiereDvd
                    filmLength = kinopoisk.filmLength



                    contentId = video_cdn.contentId
                    previewIframeSrc = video_cdn.previewIframeSrc
                    created = video_cdn.created
                    iframe_src = video_cdn.iframeSrc
                    blocked = video_cdn.blocked
                    updated = video_cdn.updated

                    countryId = video_cdn.contentId

                    imdb_rating = rating?.imdb
                    kinopoisk_raintg = rating?.kinopoisk

                    current_page = responseSerial.currentPage
                    last_page = responseSerial.lastPage
                })
            }
            return result
        }
    }
}