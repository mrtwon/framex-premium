package com.mrtwon.framex_premium.retrofit.testPOJO.responseSerial

import com.google.gson.annotations.SerializedName

data class ResponseSerial(

	@field:SerializedName("response")
	val response: List<ResponseSerialItem?>? = null,

	@field:SerializedName("lastPage")
	val lastPage: Int = 0,

	@field:SerializedName("currentPage")
	val currentPage: Int = 0
)

data class ResponseSerialItem(

	@field:SerializedName("videoCdn")
	val videoCdn: VideoCdn? = null,

	@field:SerializedName("kinopoisk")
	val kinopoisk: Kinopoisk? = null,

	@field:SerializedName("rating")
	val rating: Rating? = null,

	@field:SerializedName("_id")
	val id: String? = null
)

data class CountriesItem(

	@field:SerializedName("country")
	val country: String? = null
)

data class GenresItem(

	@field:SerializedName("genre")
	val genre: String? = null
)

data class VideoCdn(

	@field:SerializedName("end_date")
	val endDate: Any? = null,

	@field:SerializedName("episode_count")
	val episodeCount: Int? = null,

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

	@field:SerializedName("last_episode_id")
	val lastEpisodeId: Int? = null,

	@field:SerializedName("iframe_src")
	val iframeSrc: String? = null,

	@field:SerializedName("ru_title")
	val ruTitle: String = "",

	@field:SerializedName("blocked")
	val blocked: Int? = null,

	@field:SerializedName("content_type")
	val contentType: String = "",

	@field:SerializedName("id")
	val id: Int = 0,

	@field:SerializedName("iframe")
	val iframe: String? = null,

	@field:SerializedName("updated")
	val updated: String? = null,

	@field:SerializedName("season_count")
	val seasonCount: Int? = null,

	@field:SerializedName("country_id")
	val countryId: Any? = null,

	@field:SerializedName("start_date")
	val startDate: String? = null
)

data class Rating(

	@field:SerializedName("imdb")
	val imdb: String? = null,

	@field:SerializedName("kinopoisk")
	val kinopoisk: String? = null
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
	val ratingMpaa: Any? = null,

	@field:SerializedName("ratingAgeLimits")
	val ratingAgeLimits: Int? = null
)
