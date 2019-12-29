package com.app.william.youtubewithsrt

import com.app.william.youtubewithsrt.javabean.YouTubeSearch
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class SearchModel {
    var queue: String?=null
    private val searchServices: SearchServices =
        NetworkModule().retrofit().create(SearchServices::class.java)

    fun search(): Single<YouTubeSearch>? {
        return if (queue == null || queue!!.isEmpty()) null else {
            searchServices.searchVideo(YouTubeApiKey.Key, queue!!, "video", "snippet")
                .subscribeOn(Schedulers.io())
        }
    }

    fun page(pageToken: String): Single<YouTubeSearch>? {
        return if (queue == null || queue!!.isEmpty()) null else {
            searchServices.next(YouTubeApiKey.Key, queue!!, "video", "snippet", pageToken)
                .subscribeOn(Schedulers.io())
        }
    }
}