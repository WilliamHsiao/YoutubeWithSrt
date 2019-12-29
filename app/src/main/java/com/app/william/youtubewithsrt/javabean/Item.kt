package com.app.william.youtubewithsrt.javabean

import com.google.gson.annotations.SerializedName

data class Item(

	@field:SerializedName("snippet")
	val snippet: Snippet? = null,

	@field:SerializedName("kind")
	val kind: String? = null,

	@field:SerializedName("etag")
	val etag: String? = null,

	@field:SerializedName("id")
	val id: VideoType? = null
)