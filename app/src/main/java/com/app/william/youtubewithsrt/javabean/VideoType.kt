package com.app.william.youtubewithsrt.javabean

import com.google.gson.annotations.SerializedName

data class VideoType(

	@field:SerializedName("kind")
	val kind: String? = null,

	@field:SerializedName("videoId")
	val videoId: String? = null
)