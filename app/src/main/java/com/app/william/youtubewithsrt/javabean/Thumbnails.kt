package com.app.william.youtubewithsrt.javabean

import com.google.gson.annotations.SerializedName

data class Thumbnails(

	@field:SerializedName("default")
	val min: VideoSize? = null,

	@field:SerializedName("high")
	val high: VideoSize? = null,

	@field:SerializedName("medium")
	val medium: VideoSize? = null
)