package com.app.william.youtubewithsrt

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.google.android.youtube.player.YouTubePlayer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainViewModel : ViewModel() {
    private val srtHelper = SrtHelper()

    val title = MutableLiveData<String>()
    val srt: LiveData<String> = Transformations.map(srtHelper.content) { c ->
        c?.content
    }

    val strVisiable: LiveData<Int> = Transformations.map(srtHelper.content){c->
        if(c==null) View.GONE else View.VISIBLE
    }
    val videoId = MutableLiveData<String>()

    private var disposableSrt: Disposable? = null

    init {
        videoId.value = "hLQl3WQQoQ0"
        title.value = "Adele - Someone Like You"
        setStrLoad("Adele", "Someone Like You")
    }

    private fun setStrLoad(a: String, s: String) {
        srtHelper.clean()
        disposableSrt = SrtModel().getList(a, s).subscribeOn(Schedulers.io())
            .subscribe({
                srtHelper.setList(it)
            }, {
                Log.e("setStrLoad", it.message)
            })
    }

    fun playbackEventListener(player: YouTubePlayer): YouTubePlayer.PlaybackEventListener {
        return object : YouTubePlayer.PlaybackEventListener {
            override fun onSeekTo(p0: Int) {
            }

            override fun onBuffering(p0: Boolean) {
                if (p0)
                    srtHelper.stop()
            }

            override fun onPlaying() {
                srtHelper.start(player.currentTimeMillis)
            }

            override fun onStopped() {
                srtHelper.stop()
                player.play()

            }

            override fun onPaused() {
                srtHelper.stop()
            }
        }

    }

    fun changeVideo(data: Intent) {
        videoId.postValue(data.getStringExtra("videoId"))
        title.postValue("${data.getStringExtra("artist")} - ${data.getStringExtra("song")}")
        setStrLoad(data.getStringExtra("artist"), data.getStringExtra("song"))

    }

    override fun onCleared() {
        super.onCleared()
        disposableSrt?.dispose()
    }
}