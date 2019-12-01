package com.app.william.youtubewithsrt

data class Time(
    var hour:Int=0,
    var minute:Int=0,
    var second:Int=0,
    var millisecond:Int=0
){
    val milli:Int get() = millisecond+1000*second+1000*60*minute+1000*60*60*hour


}