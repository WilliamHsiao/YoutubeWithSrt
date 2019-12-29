package com.app.william.youtubewithsrt

import com.app.william.youtubewithsrt.javabean.YouTubeSearch
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class SearchModel() {
    var queue: String?=null
    private val searchServices: SearchServices =
        NetworkModule().retrofit().create(SearchServices::class.java)

    fun search(): Single<YouTubeSearch>? {
        if (queue == null || queue!!.isEmpty()) return null else {
            return searchServices.searchVideo(YouTubeApiKey.Key, queue!!, "video", "snippet")
                .subscribeOn(Schedulers.io())
        }
    }

    fun page(pageToken: String): Single<YouTubeSearch>? {
        if (queue == null || queue!!.isEmpty()) return null else {
            return searchServices.next(YouTubeApiKey.Key, queue!!, "video", "snippet", pageToken)
                .subscribeOn(Schedulers.io())
        }
    }
}