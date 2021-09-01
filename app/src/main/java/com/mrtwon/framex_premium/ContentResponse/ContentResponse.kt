package com.mrtwon.framex_premium.ContentResponse

open class ContentResponse {
   //base info
   var id: Int = 0
   var imdb_id: String? = null
   var kp_id: String? = null
   var ru_title: String = ""
   var description: String? = null
   var orig_title: String? = null
   var year: Int? = null
   var contentType: String = ""
   var poster: String? = null
   var poster_preview: String? = null
   var iframe_src: String? = null

   //rating
   var imdb_rating: String? = null
   var kinopoisk_raintg: String? = null

   var last_page: Int = 0
   var current_page: Int = 0
}