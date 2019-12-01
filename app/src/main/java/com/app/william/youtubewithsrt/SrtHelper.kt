package com.app.william.youtubewithsrt

import android.os.Handler
import androidx.lifecycle.MutableLiveData

class SrtHelper {
    private var list: List<Srt>? = null
    private val handlerStart: Handler = Handler()
    private val handlerEnd: Handler = Handler()
    val content: MutableLiveData<Srt?> = MutableLiveData()

    private var srt: Srt? = null

    private var runnableStart: Runnable = Runnable {
        content.postValue(srt)
    }

    private var runnableEnd: Runnable = Runnable {
        content.postValue(null)

        setNext(if (srt == null) 0 else srt!!.endTime.milli)
    }

    fun setList(l: List<Srt>) {
        list = l
    }

    fun stop() {
        handlerStart.removeCallbacks(runnableStart)
        handlerEnd.removeCallbacks(runnableEnd)
    }

    fun start(time: Int) {
        stop()

        srt = getSrt(time)

        if (srt == null) {
            setNext(time)
        } else {
            content.postValue(srt)
            val delay: Long = (srt!!.endTime.milli - time).toLong()
            handlerEnd.postDelayed(runnableEnd, delay)
        }
    }

    private fun setNext(time: Int) {
        srt = getNextSrt(time)

        srt?.let {
            handlerStart.postDelayed(runnableStart, (srt!!.startTime.milli - time).toLong())
            handlerEnd.postDelayed(runnableEnd, (srt!!.endTime.milli - time).toLong())
        }
    }

    private fun getNextSrt(time: Int): Srt? {
        list?.forEach {
            if (it.startTime.milli > time)
                return it
        }
        return null
    }

    private fun getSrt(time: Int): Srt? {
        list?.forEach {
            if (it.startTime.milli <= time && it.endTime.milli >= time)
                return it
        }
        return null
    }

}