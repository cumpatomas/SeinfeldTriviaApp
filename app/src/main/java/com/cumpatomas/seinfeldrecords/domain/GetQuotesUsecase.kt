package com.cumpatomas.seinfeldrecords.domain

import androidx.compose.runtime.mutableStateOf
import com.cumpatomas.seinfeldrecords.ui.quotesfragment.QuoteItem
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject


class GetQuotesUsecase @Inject constructor(
    private val scrapScript: ScrapScripts,
    private val getscript: GetRandomScript,
) {

    suspend operator fun invoke(): List<QuoteItem> {
        val quotesList = mutableSetOf<String>()
        val quotesItemsList = mutableListOf<QuoteItem>()
        coroutineScope {

            repeat(5) {
                launch {
                    val lines = getscript.invoke(scrapScript.invoke().shuffled().random()).filter {
                        it.contains("""(George:|Elaine:|Kramer:|Jerry:|GEORGE:|ELAINE:|KRAMER:|JERRY:)""".toRegex())
                    }

                    quotesList += lines

                }.join()
            }
//                println("lines $quotesList")
            quotesList.forEach {

                if (it.length > 40 && it.contains("""(George:| GEORGE:)""".toRegex())) {
                        quotesItemsList += QuoteItem(
                            quote = it.substringAfter(':'),
                            author = "George",

                        )

                } else if (it.length > 40 && it.contains("""(Elaine:| ELAINE:)""".toRegex())) {
                        quotesItemsList += QuoteItem(
                            quote = it.substringAfter(':'),
                            author = "Elaine",

                        )
                } else if (it.length > 40 && it.contains("""(Jerry:| JERRY:)""".toRegex())) {
                        quotesItemsList += QuoteItem(
                            quote = it.substringAfter(':'),
                            author = "Jerry",

                        )
                } else if (it.length > 40 && it.contains("""(Kramer:| KRAMER:)""".toRegex())) {
                        quotesItemsList += QuoteItem(
                            quote = it.substringAfter(':'),
                            author = "Kramer",
                        )
                }
            }

        }
        println("Quotes: $quotesItemsList")

        return if (quotesItemsList.size > 2) quotesItemsList.distinct().shuffled() else invoke()

//        return quotesItemsList.distinct().shuffled()
    }

}
