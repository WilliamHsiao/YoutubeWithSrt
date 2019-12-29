package com.app.william.youtubewithsrt.javabean

import com.google.gson.annotations.SerializedName

data class VideoSize(

	@field:SerializedName("width")
	val width: Int? = null,

	@field:SerializedName("url")
	val url: String? = null,

	@field:SerializedName("height")
	val height: Int? = null
)