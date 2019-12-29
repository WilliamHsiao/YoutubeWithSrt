package com.app.william.youtubewithsrt

import com.app.william.youtubewithsrt.javabean.YouTubeSearch
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchServices {
    @GET("youtube/v3/search")
    fun searchVideo(@Query("key") key: String,@Query("q") q: String,@Query("type") type: String,@Query("part") part: String): Single<YouTubeSearch>

    @GET("youtube/v3/search")
    fun next(@Query("key") key: String,@Query("q") q: String,@Query("type") type: String,@Query("part") part: String,@Query("pageToken") pageToken: String): Single<YouTubeSearch>

}