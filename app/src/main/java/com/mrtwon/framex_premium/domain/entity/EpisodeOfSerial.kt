package com.mrtwon.framex_premium.domain.entity

data class EpisodeOfSerial(val idContent: Int,val allEpisodeCount: Int, val list: List<OneEpisode>)
data class OneEpisode(val season: String, val episode: String)