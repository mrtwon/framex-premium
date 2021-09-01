package com.mrtwon.framex_premium.ContentResponse

import com.mrtwon.framex_premium.retrofit.framexPojo.responseMovie.*

class Movie: ContentResponse(){

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
        fun buildMovie(responseMovie: ResponseMovie?): Movie?{
            if(responseMovie == null) return null
            val video_cdn = responseMovie.response?.get(0)?.videoCdn ?: return null
            val kinopoisk = responseMovie.response.get(0)?.kinopoisk ?: return null
            val rating = responseMovie.response.get(0)?.rating
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

                current_page = responseMovie.currentPage
                last_page = responseMovie.lastPage

                poster_preview = posterUrlPreview
            }
        }
        fun buildMovies(responseMovie: ResponseMovie?): List<Movie>{
            val result = arrayListOf<Movie>()
            val size = responseMovie?.response?.size ?: return result
            for(i in 0 until size){
                val video_cdn = responseMovie.response.get(i)?.videoCdn ?: return result
                val kinopoisk = responseMovie.response.get(i)?.kinopoisk ?: return result
                val rating = responseMovie.response.get(i)?.rating
                result.add(Movie().apply {
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

                    current_page = responseMovie.currentPage
                    last_page = responseMovie.lastPage

                    poster_preview = posterUrlPreview
                })
            }
            return result
        }
    }
}