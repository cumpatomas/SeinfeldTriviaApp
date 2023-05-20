package com.cumpatomas.seinfeldrecords.domain

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class GetRandomScript() {
    val listoOfLinks = listOf<String>(
        "https://www.seinfeldscripts.com/TheWigMaster.htm",
        "https://www.seinfeldscripts.com/TheApartment.htm",
        "https://www.seinfeldscripts.com/TheMasseuse.html",
        "https://www.seinfeldscripts.com/TheSeven.html",
        "https://www.seinfeldscripts.com/TheDoll.htm",
        "https://www.seinfeldscripts.com/TheLipReader.htm"

    )

    suspend operator fun invoke(link: String): List<String> {
        val randomLines = mutableListOf<String>()
        coroutineScope {
            launch {
                val url = Jsoup.connect(link).get()
                val title = url.getElementsByTag("h1")
                val body = url.getElementsByTag("p").eachText()
                val filteredBody = body.filter { it.contains(":") }
                randomLines.add(title.text())
                for (i in filteredBody.drop(10)) {
                    randomLines.add(i)
                }
            }
        }
        /**
         * some scripts can't be scraped as they have different html format so in that case
         * we implement this function that take a random script from possibles episodes
         */
        return if (randomLines.size > 2) randomLines else {
            println("got ya!")
            invoke(listoOfLinks.shuffled().random())
        }
    }
}