package com.app.william.youtubewithsrt

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter

object ImageBinding {
    @BindingAdapter("src")
    @JvmStatic
    fun setSrc(view: ImageView, bitmap: Bitmap? ) {

        if(bitmap==null){
            view.setImageResource(android.R.drawable.stat_sys_download_done)

        }else{

            view.setImageBitmap(bitmap)
        }
    }
}