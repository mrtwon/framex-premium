package com.mrtwon.framex_premium.retrofit.framexAuth

import com.google.gson.annotations.SerializedName

data class ResponseUser(

	@field:SerializedName("response")
	val response: List<ResponseItem>
)

data class ResponseItem(

	@field:SerializedName("uid")
	val uid: String,

	@field:SerializedName("user_type")
	val userType: String,

	@field:SerializedName("favorite_content")
	val favoriteContent: String,

	@field:SerializedName("nickname")
	val nickname: String,

	@field:SerializedName("isDonate")
	val isDonate: Boolean,

	@field:SerializedName("about")
	val about: String,

	@field:SerializedName("isBlocked")
	val isBlocked: Boolean,

	@field:SerializedName("_id")
	val id: String
)
