package com.app.william.youtubewithsrt


data class Srt(
    var content: String = "",
    var startTime: Time = Time(),
    var endTime: Time = Time()
)