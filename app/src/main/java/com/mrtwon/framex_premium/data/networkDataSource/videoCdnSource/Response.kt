package com.mrtwon.framex_premium.data.networkDataSource.videoCdnSource

import com.google.gson.annotations.SerializedName

data class ContentResponse(

	@field:SerializedName("per_page")
	val perPage: Int,

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("last_page")
	val lastPage: Int,

	@field:SerializedName("next_page_url")
	val nextPageUrl: Any,

	@field:SerializedName("prev_page_url")
	val prevPageUrl: Any,

	@field:SerializedName("result")
	val result: Boolean,

	@field:SerializedName("first_page_url")
	val firstPageUrl: String,

	@field:SerializedName("path")
	val path: String,

	@field:SerializedName("total")
	val total: Int,

	@field:SerializedName("last_page_url")
	val lastPageUrl: String,

	@field:SerializedName("from")
	val from: Int,

	@field:SerializedName("to")
	val to: Int,

	@field:SerializedName("current_page")
	val currentPage: Int
)

data class EpisodesItem(

	@field:SerializedName("orig_title")
	val origTitle: String,

	@field:SerializedName("imdb_id")
	val imdbId: String,

	@field:SerializedName("kinopoisk_id")
	val kinopoiskId: String,

	@field:SerializedName("created")
	val created: String,

	@field:SerializedName("num")
	val num: String,

	@field:SerializedName("ru_released")
	val ruReleased: String,

	@field:SerializedName("season_id")
	val seasonId: Int,

	@field:SerializedName("season_num")
	val seasonNum: Int,

	@field:SerializedName("media")
	val media: List<MediaItem>,

	@field:SerializedName("tv_series_id")
	val tvSeriesId: Int,

	@field:SerializedName("ru_title")
	val ruTitle: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("released")
	val released: String
)

data class Translation(

	@field:SerializedName("short_title")
	val shortTitle: String,

	@field:SerializedName("shorter_title")
	val shorterTitle: String,

	@field:SerializedName("smart_title")
	val smartTitle: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("source_quality")
	val sourceQuality: String,

	@field:SerializedName("iframe_src")
	val iframeSrc: String,

	@field:SerializedName("iframe")
	val iframe: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("priority")
	val priority: Int,

	@field:SerializedName("max_quality")
	val maxQuality: Int,

	@field:SerializedName("episodes_count")
	val episodesCount: Int
)

data class MediaItem(

	@field:SerializedName("translation_id")
	val translationId: Int,

	@field:SerializedName("content_id")
	val contentId: Int,

	@field:SerializedName("created")
	val created: String,

	@field:SerializedName("accepted")
	val accepted: String,

	@field:SerializedName("max_quality")
	val maxQuality: Int,

	@field:SerializedName("deleted_at")
	val deletedAt: Any,

	@field:SerializedName("duration")
	val duration: Int,

	@field:SerializedName("tv_series_id")
	val tvSeriesId: Int,

	@field:SerializedName("path")
	val path: String,

	@field:SerializedName("content_type")
	val contentType: String,

	@field:SerializedName("blocked")
	val blocked: Int,

	@field:SerializedName("translation")
	val translation: Translation,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("source_quality")
	val sourceQuality: String
)

data class DataItem(

	@field:SerializedName("end_date")
	val endDate: Any,

	@field:SerializedName("episode_count")
	val episodeCount: Int,

	@field:SerializedName("orig_title")
	val origTitle: String,

	@field:SerializedName("imdb_id")
	val imdbId: String,

	@field:SerializedName("kinopoisk_id")
	val kinopoiskId: String,

	@field:SerializedName("preview_iframe_src")
	val previewIframeSrc: String,

	@field:SerializedName("created")
	val created: String,

	@field:SerializedName("last_episode_id")
	val lastEpisodeId: Int,

	@field:SerializedName("iframe_src")
	val iframeSrc: String,

	@field:SerializedName("ru_title")
	val ruTitle: String,

	@field:SerializedName("blocked")
	val blocked: Int,

	@field:SerializedName("translations")
	val translations: List<TranslationsItem>,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("iframe")
	val iframe: String,

	@field:SerializedName("updated")
	val updated: String,

	@field:SerializedName("season_count")
	val seasonCount: Int,

	@field:SerializedName("episodes")
	val episodes: List<EpisodesItem>,

	@field:SerializedName("start_date")
	val startDate: String
)

data class TranslationsItem(

	@field:SerializedName("short_title")
	val shortTitle: String,

	@field:SerializedName("shorter_title")
	val shorterTitle: String,

	@field:SerializedName("smart_title")
	val smartTitle: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("source_quality")
	val sourceQuality: String,

	@field:SerializedName("iframe_src")
	val iframeSrc: String,

	@field:SerializedName("iframe")
	val iframe: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("priority")
	val priority: Int,

	@field:SerializedName("max_quality")
	val maxQuality: Int,

	@field:SerializedName("episodes_count")
	val episodesCount: Int
)
