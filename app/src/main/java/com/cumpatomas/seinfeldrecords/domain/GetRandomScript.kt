package com.cumpatomas.seinfeldrecords.domain

import android.util.Log
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class GetRandomScript() {
    val listoOfLinks = listOf<String>(
        "https://www.seinfeldscripts.com/TheWigMaster.htm",
        "https://www.seinfeldscripts.com/TheApartment.htm",
        "https://www.seinfeldscripts.com/TheSeven.html",
        "https://www.seinfeldscripts.com/TheDoll.htm",
        "https://www.seinfeldscripts.com/TheLipReader.htm"
    )
    suspend operator fun invoke(link: String): List<String> {
        var randomLines = mutableListOf<String>()
        var output = mutableListOf<String>()
        coroutineScope {
            launch {
                val url = Jsoup.connect(link).get()
                val title = url.getElementsByTag("h1")
                val body = url.getElementsByTag("p").eachText()
                randomLines.add(title.text())
                for (i in body.drop(10)) {
                    randomLines.add(i)
                }
                Log.d("random", randomLines.joinToString())
            }.join()
            val randomIndex = (20..(randomLines.lastIndex - 10)).random()
            val outputList = mutableListOf<String>(randomLines[0])
            if (randomLines[0].length > 20 || randomLines[0].contains(":") || randomLines.size < 5) {
                coroutineScope {
                    launch {
                        invoke(listoOfLinks.random())
                    }.join()
                }
            }
            randomLines =
                (outputList + randomLines.subList(randomIndex - 6, randomIndex + 6)).toMutableList()
        }
        return randomLines
    }
}

