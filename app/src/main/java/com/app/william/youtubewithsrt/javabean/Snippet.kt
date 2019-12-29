package com.app.william.youtubewithsrt.javabean

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.app.william.youtubewithsrt.BR
import com.google.gson.annotations.SerializedName
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.io.InputStream
import java.net.URL

data class Snippet(

	@field:SerializedName("publishedAt")
	val publishedAt: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("thumbnails")
	val thumbnails: Thumbnails? = null,

	@field:SerializedName("channelId")
	val channelId: String? = null,

	@field:SerializedName("channelTitle")
	val channelTitle: String? = null,

	@field:SerializedName("liveBroadcastContent")
	val liveBroadcastContent: String? = null,

			private var _bitmap: Bitmap? = null
) : BaseObservable() {
	private val single: Single<Bitmap> = Single.create { emitter ->
		try {
			val img = URL(thumbnails?.medium?.url).content as InputStream
			emitter.onSuccess(BitmapFactory.decodeStream(img))
		} catch (e: Exception) {
			emitter.onError(e)
		}
	}

	var bitmap: Bitmap?
		@SuppressLint("CheckResult") @Bindable get() {
			if(_bitmap==null){
				 single.subscribeOn(Schedulers.io())
					.subscribe(
						{b-> bitmap=b}
						,{e-> Log.e("Snippet",e.message)})
			}
			return _bitmap
		}
		set(value) {
			_bitmap = value
			notifyPropertyChanged(BR.bitmap)
		}
}