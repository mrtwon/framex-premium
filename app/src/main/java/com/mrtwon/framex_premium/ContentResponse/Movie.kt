package com.mrtwon.framex_premium.ContentResponse

import com.mrtwon.framex_premium.Retrofit.TestPOJO.ResponseMovie.*

class Movie: Content(){

    //kinopoisk
    var premiereDvd: Any? = null
    var filmLength: String? = null
    var countries: List<CountriesItem?>? = null
    var type: String? = null
    var premiereWorld: String? = null
    var webUrl: String? = null
    var premiereRu: String? = null
    var genres: List<GenresItem?>? = null
    var premiereWorldCountry: String? = null
    var posterUrlPreview: String? = null
    var ratingMpaa: String? = null
    var ratingAgeLimits: Int? = null

    //video cdn
    var defaultMediaId: Any? = null
    var contentId: Any? = null
    var previewIframeSrc: String? = null
    var created: String? = null
    var blocked: Int? = null
    var updated: String? = null
    var released: String? = null
    var countryId: Any? = null

    companion object{
        fun buildMovie(responseMovie: ResponseMovieItem?): Movie?{
            if(responseMovie == null) return null
            val video_cdn = responseMovie.videoCdn ?: return null
            val kinopoisk = responseMovie.kinopoisk ?: return null
            val rating = responseMovie.rating
            return Movie().apply {
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

                premiereDvd = kinopoisk.premiereDvd
                filmLength = kinopoisk.filmLength
                countries = kinopoisk.countries
                type = kinopoisk.type
                premiereWorld = kinopoisk.premiereWorld
                poster = kinopoisk.posterUrl
                webUrl = kinopoisk.webUrl
                premiereRu = kinopoisk.premiereRu
                genres = kinopoisk.genres
                premiereWorldCountry = kinopoisk.premiereWorldCountry
                posterUrlPreview = kinopoisk.posterUrlPreview
                ratingMpaa = kinopoisk.ratingMpaa
                ratingAgeLimits = kinopoisk.ratingAgeLimits

                defaultMediaId = video_cdn.defaultMediaId
                contentId = video_cdn.contentId
                previewIframeSrc = video_cdn.previewIframeSrc
                created = video_cdn.created
                iframe_src = video_cdn.iframeSrc
                blocked = video_cdn.blocked
                updated = video_cdn.updated
                released = video_cdn.released
                countryId = video_cdn.contentId

                imdb_rating = rating?.imdb
                kinopoisk_raintg = rating?.kinopoisk
            }
        }
    }
}