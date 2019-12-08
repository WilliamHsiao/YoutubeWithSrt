package com.app.william.youtubewithsrt

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val srtHelper = SrtHelper()

    private var youTubePlayerFragment: YouTubePlayerSupportFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        srtHelper.content.observe(this, Observer {
            if (it == null) {
                textView.visibility = GONE
            } else {
                textView.visibility = VISIBLE
                textView.text = it.content
            }
        })

        youTubePlayerFragment =
            supportFragmentManager.findFragmentById(R.id.youtubeView) as YouTubePlayerSupportFragment?

        youTubePlayerFragment!!.initialize(
            YouTubeApiKey.Key,
            object : YouTubePlayer.OnInitializedListener {
                override fun onInitializationSuccess(
                    p0: YouTubePlayer.Provider?,
                    player: YouTubePlayer,
                    p2: Boolean
                ) {

                    player.apply {
                        cueVideo("hLQl3WQQoQ0")
                        setPlaybackEventListener(object : YouTubePlayer.PlaybackEventListener {
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
                        })
                    }


                }

                override fun onInitializationFailure(
                    p0: YouTubePlayer.Provider?,
                    p1: YouTubeInitializationResult?
                ) {
                    when (p1) {
                        YouTubeInitializationResult.SERVICE_MISSING ->
                            try {
                                startActivity(Intent(Intent.ACTION_VIEW).apply {
                                    data =
                                        Uri.parse("market://details?id=com.google.android.youtube")
                                })
                            } catch (e: Exception) {
                            }
                        else -> Log.e("YouTubePlayer", p1?.name)
                    }

                }
            })

        SrtModel().getList().subscribeOn(Schedulers.io())
            .subscribe(object : SingleObserver<List<Srt>> {
                override fun onSuccess(t: List<Srt>) {
                    srtHelper.setList(t)
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {

                }
            })

    }


}
