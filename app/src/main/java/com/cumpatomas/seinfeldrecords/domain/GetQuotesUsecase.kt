package com.cumpatomas.seinfeldrecords.domain

import com.cumpatomas.seinfeldrecords.ui.quotesfragment.QuoteItem
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class GetQuotesUsecase @Inject constructor(
    private val scrapScript: ScrapScripts,
    private val getscript: GetRandomScript,
) {
    suspend operator fun invoke(link: String): List<QuoteItem> {
        val quotesList = mutableSetOf<String>()
        val quotesItemsList = mutableListOf<QuoteItem>()
        coroutineScope {
            repeat(3) {
                launch {
                    val lines = getscript.invoke(link).filter {
                        it.contains("""(George:|Elaine:|Kramer:|Jerry:|GEORGE:|ELAINE:|KRAMER:|JERRY:)""".toRegex())
                    }

                    quotesList += lines
                }.join()
            }
//                println("lines $quotesList")
            quotesList.forEach {
                if (it.length > 50 && it.contains("""(George:| GEORGE:)""".toRegex()) && it.last() == '.') {
                    quotesItemsList += QuoteItem(
                        quote = "\"" + it.substringAfter(':') + "\"" ,
                        author = "George",
                        )
                } else if (it.length > 50 && it.contains("""(Elaine:| ELAINE:)""".toRegex()) && it.last() == '.') {
                    quotesItemsList += QuoteItem(
                        quote = "\""  + it.substringAfter(':') + "\"" ,
                        author = "Elaine",
                        )
                } else if (it.length > 50 && it.contains("""(Jerry:| JERRY:)""".toRegex())&& it.last() == '.') {
                    quotesItemsList += QuoteItem(
                        quote = "\"" + it.substringAfter(':') + "\"" ,
                        author = "Jerry",
                        )
                } else if (it.length > 50 && it.contains("""(Kramer:| KRAMER:)""".toRegex())&& it.last() == '.') {
                    quotesItemsList += QuoteItem(
                        quote = "\"" + it.substringAfter(':') + "\"" ,
                        author = "Kramer",
                    )
                }
            }
        }
        println("Quotes: $quotesItemsList")

        return if (quotesItemsList.size > 2) quotesItemsList.distinct().shuffled() else emptyList()
//        return quotesItemsList.distinct().shuffled()
    }
}
