package com.app.william.youtubewithsrt

import io.reactivex.Single
import io.reactivex.Single.create
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.parser.Parser
import java.net.URLEncoder

class SrtModel {
    private var artist:String=""
    private var song:String=""
    private fun getDocument() :Single<Document>{

        return create{

            if(artist.isEmpty() || song.isEmpty())
                it.onError(Throwable("getDocument Error"))

            val document: Document? =
                Jsoup.connect("https://www.rentanadviser.com/en/subtitles/getsubtitle.aspx?artist=${toSearch(artist)}&song=${toSearch(song)}&type=srt")
                    .maxBodySize(0).get()

            if(document==null){
                it.onError(Throwable("getDocument Error"))
            }else {
                it.onSuccess(document)
            }
        }
    }

    private fun getContent(document:Document):Single<String>{
        val element:Element?=document.getElementById("ctl00_ContentPlaceHolder1_lblSubtitle")

        return if(element==null){
            resetSongName()
            getContent()
        }else{
            create{
                it.onSuccess(element.html())
            }
        }


    }

    private fun getList(str:String): Single<List<Srt>> {
        return create{
            try {

                val arrText = Parser.unescapeEntities(str, false).split("<br>")

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
                                    endTime = if (e.milli < startTime.milli) startTime else e
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

    private fun getContent(): Single<String>{
        return  getDocument()
            .retry()
            .flatMap{t-> getContent(t)}
    }

    fun getList(a:String,s:String): Single<List<Srt>> {
        //Adele
        //Someone%20Like%20You
        artist=a
        song=s
        return getContent()
            .flatMap {t->getList(t)}

    }

    private fun toSearch(str:String):String{
        return URLEncoder.encode(str, "UTF-8")
    }

    private fun resetSongName(){
        val lastIndex:Int=song.lastIndexOf(" ")
        song = if(lastIndex==-1){
            ""
        }else{
            song.substring(0,lastIndex)
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
