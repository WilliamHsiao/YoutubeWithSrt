package com.app.william.youtubewithsrt

import io.reactivex.Single
import io.reactivex.Single.create
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.parser.Parser

class SrtModel {

    fun getList(): Single<List<Srt>> {
        return create {
            try {
                val document: Document? =
                    Jsoup.connect("https://www.rentanadviser.com/en/subtitles/getsubtitle.aspx?artist=Adele&song=Someone%20Like%20You&type=srt")
                        .maxBodySize(0).get()

                val content: String =
                    document!!.getElementById("ctl00_ContentPlaceHolder1_lblSubtitle").html()

                val arrText = Parser.unescapeEntities(content, false).split("<br>")

                val list: MutableList<Srt> = ArrayList()

                var srt: Srt? = null

                for (s in arrText) {

                    if (s.contains("-->")) {
                        val arrTime = s.split("-->")
                        if (arrTime.count() == 2) {
                            val st: String = toTimeString(arrTime[0])
                            val et: String = toTimeString(arrTime[1])

                            if (st.isNotEmpty() && et.isNotEmpty()) {
                                srt = Srt().apply {
                                    startTime = toTime(st)

                                    val e = toTime(et)
                                    endTime = if (e.milli < startTime.minute) startTime else e
                                }
                                list.add(srt)
                            }
                        }

                    } else if (s.isBlank()) {
                        srt = null
                    } else if (s.isNotBlank()) {
                        srt?.let { temp ->
                            if (temp.content.isBlank()) {
                                temp.content = s
                            } else {
                                temp.content += "\n" //顯示時再次分行
                                temp.content += s
                            }
                        }
                    }

                }


                it.onSuccess(list)
            } catch (e: Throwable) {
                it.onError(e)
                return@create
            }
        }

    }

    private fun toTimeString(s: String): String {

        if (s.trim().matches("\\d+:\\d+:\\d+‚\\d+".toRegex())) {
            return s.trim()
        }

        return ""
    }

    private fun toTime(s: String): Time {
        return Time().apply {
            val arrS = s.replace(" ", "").split(":")
            val arrSec = arrS[2].split("‚")
            hour = Integer.parseInt(arrS[0])
            minute = Integer.parseInt(arrS[1])
            second = Integer.parseInt(arrSec[0])
            millisecond = Integer.parseInt(arrSec[1])

        }
    }
}