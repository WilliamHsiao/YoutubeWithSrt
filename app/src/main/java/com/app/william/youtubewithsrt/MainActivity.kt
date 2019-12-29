package com.app.william.youtubewithsrt

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
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
    private var player: YouTubePlayer? = null

    private var videoId: String = "hLQl3WQQoQ0"

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

        initFragment()


        button.text = "Adele - Someone Like You"
        setStrLoad("Adele", "Someone Like You")
    }

    fun search(view: View) {
        startActivityForResult(Intent(this, SearchActivity::class.java), 0)
    }

    private fun initFragment(){
        youTubePlayerFragment!!.initialize(
            YouTubeApiKey.Key,
            object : YouTubePlayer.OnInitializedListener {
                override fun onInitializationSuccess(
                    p0: YouTubePlayer.Provider?,
                    p1: YouTubePlayer,
                    p2: Boolean
                ) {
                    player = p1
                    player?.apply {
                        cueVideo(videoId)
                        setPlaybackEventListener(object : YouTubePlayer.PlaybackEventListener {
                            override fun onSeekTo(p0: Int) {
                            }

                            override fun onBuffering(p0: Boolean) {
                                if (p0)
                                    srtHelper.stop()
                            }

                            override fun onPlaying() {
                                srtHelper.start(player!!.currentTimeMillis)
                            }

                            override fun onStopped() {
                                srtHelper.stop()
                                player!!.play()

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

    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            0 -> {
                videoId = data!!.getStringExtra("videoId")
                player?.release()
                initFragment()
                button.text = "${data.getStringExtra("artist")} - ${data.getStringExtra("song")}"
                setStrLoad(data.getStringExtra("artist"), data.getStringExtra("song"))
            }

        }

    }

    private fun setStrLoad(a: String, s: String) {
        srtHelper.clean()
        SrtModel().getList(a, s).subscribeOn(Schedulers.io())
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
