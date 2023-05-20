package com.cumpatomas.seinfeldrecords.domain

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class ScrapScripts {

    suspend operator fun invoke(): MutableList<String> {
        val outputList = mutableListOf<String>()
        coroutineScope {
            launch {
                val url = Jsoup.connect("https://www.seinfeldscripts.com/seinfeld-scripts.html")
                    .get();
                val table = url.getElementsByAttributeStarting("href")

                for (i in 15..table.lastIndex - 16) {
                    if (table[i].toString().contains(".htm")) {
                        outputList.add("https://www.seinfeldscripts.com/" + table[i].toString().substringAfter("\"").substringBefore("\"").trim())

                    }
                }
            }
        }
        return outputList

    }

}