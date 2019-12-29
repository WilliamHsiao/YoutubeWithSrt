package com.app.william.youtubewithsrt

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.william.youtubewithsrt.databinding.ActivityMainBinding
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment


class MainActivity : AppCompatActivity() {


    private var youTubePlayerFragment: YouTubePlayerSupportFragment? = null
    private var player: YouTubePlayer? = null
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    private val searchFragment = SearchFragment()

    //   private var videoId: String = "hLQl3WQQoQ0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.model = viewModel
        binding.lifecycleOwner = this

        youTubePlayerFragment =
            supportFragmentManager.findFragmentById(R.id.youtubeView) as YouTubePlayerSupportFragment?

        viewModel.videoId.observe(this, Observer {
            if (it != null) {
                player?.release()
                initFragment(it)
            }
        })
    }

    fun search(view: View) {
        //  startActivityForResult(Intent(this, SearchActivity::class.java), 0)
        searchFragment.show(supportFragmentManager, "search")
    }

    private fun initFragment(videoId: String) {
        youTubePlayerFragment!!.initialize(
            YouTubeApiKey.Key,
            object : YouTubePlayer.OnInitializedListener {
                override fun onInitializationSuccess(
                    p0: YouTubePlayer.Provider?,
                    p1: YouTubePlayer,
                    p2: Boolean
                ) {
                    player = p1
                    try {
                        player?.apply {
                            cueVideo(videoId)
                            setPlaybackEventListener(viewModel.playbackEventListener(this))
                        }
                    } catch (e: java.lang.Exception) {
                        Log.e("initFragment", e.message)
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
                if (data != null) viewModel.changeVideo(data)
            }

        }

    }

}
