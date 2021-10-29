package com.mrtwon.framex_premium.retrofit.framexAuth

import com.google.gson.annotations.SerializedName


data class ResponseError(

	@field:SerializedName("response")
	val response: Response
)

data class Response(

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("message")
	val message: String
)
