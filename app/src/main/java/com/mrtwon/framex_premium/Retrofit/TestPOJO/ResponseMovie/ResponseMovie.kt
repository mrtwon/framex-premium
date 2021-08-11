package com.mrtwon.framex_premium.Retrofit.TestPOJO.ResponseMovie

import com.google.gson.annotations.SerializedName

data class ResponseMovie(

	@field:SerializedName("response")
	val response: List<ResponseMovieItem?>? = null
)

data class Kinopoisk(

	@field:SerializedName("year")
	val year: Int? = null,

	@field:SerializedName("premiereDvd")
	val premiereDvd: Any? = null,

	@field:SerializedName("filmLength")
	val filmLength: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("countries")
	val countries: List<CountriesItem?>? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("premiereWorld")
	val premiereWorld: String? = null,

	@field:SerializedName("posterUrl")
	val posterUrl: String? = null,

	@field:SerializedName("webUrl")
	val webUrl: String? = null,

	@field:SerializedName("premiereRu")
	val premiereRu: String? = null,

	@field:SerializedName("genres")
	val genres: List<GenresItem?>? = null,

	@field:SerializedName("premiereWorldCountry")
	val premiereWorldCountry: String? = null,

	@field:SerializedName("posterUrlPreview")
	val posterUrlPreview: String? = null,

	@field:SerializedName("ratingMpaa")
	val ratingMpaa: String? = null,

	@field:SerializedName("ratingAgeLimits")
	val ratingAgeLimits: Int? = null
)

data class CountriesItem(

	@field:SerializedName("country")
	val country: String? = null
)

data class ResponseMovieItem(

	@field:SerializedName("videoCdn")
	val videoCdn: VideoCdn? = null,

	@field:SerializedName("kinopoisk")
	val kinopoisk: Kinopoisk? = null,

	@field:SerializedName("rating")
	val rating: Rating? = null,

	@field:SerializedName("_id")
	val id: String? = null
)

data class GenresItem(

	@field:SerializedName("genre")
	val genre: String? = null
)

data class VideoCdn(

	@field:SerializedName("default_media_id")
	val defaultMediaId: Any? = null,

	@field:SerializedName("orig_title")
	val origTitle: String? = null,

	@field:SerializedName("imdb_id")
	val imdbId: String? = null,

	@field:SerializedName("kinopoisk_id")
	val kinopoiskId: String? = null,

	@field:SerializedName("content_id")
	val contentId: Any? = null,

	@field:SerializedName("preview_iframe_src")
	val previewIframeSrc: String? = null,

	@field:SerializedName("created")
	val created: String? = null,

	@field:SerializedName("iframe_src")
	val iframeSrc: String? = null,

	@field:SerializedName("ru_title")
	val ruTitle: String? = null,

	@field:SerializedName("blocked")
	val blocked: Int? = null,

	@field:SerializedName("content_type")
	val contentType: String? = null,

	@field:SerializedName("id")
	val id: Int = 0,

	@field:SerializedName("iframe")
	val iframe: String? = null,

	@field:SerializedName("updated")
	val updated: String? = null,

	@field:SerializedName("released")
	val released: String? = null,

	@field:SerializedName("country_id")
	val countryId: Any? = null
)

data class Rating(

	@field:SerializedName("imdb")
	val imdb: String? = null,

	@field:SerializedName("kinopoisk")
	val kinopoisk: String? = null
)
