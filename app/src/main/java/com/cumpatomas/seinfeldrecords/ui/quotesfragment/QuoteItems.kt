package com.cumpatomas.seinfeldrecords.ui.quotesfragment

import androidx.annotation.DrawableRes
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.cumpatomas.seinfeldrecords.R

data class QuoteItem(
    val quote: String,
    val author: String,
    var isAnswered: MutableState<Boolean> = mutableStateOf(false)
)

val quotesList = listOf(
    QuoteItem("That's a shame", "Jerry"),
    QuoteItem(
        "Who's gonna turn down a Junior Mint? It's chocolate, it's peppermint—it's delicious",
        "Kramer"
    ),
    QuoteItem("I like to think I have a little grace", "Elaine"),
    QuoteItem("The sea was angry that day, my friends", "George"),
)

data class Person(
    val name: String,
    @DrawableRes val profile: Int
)

val author = listOf(
    Person(Character.Jerry.name, R.drawable.jerry_png),
    Person(Character.Elaine.name, R.drawable.elaine_png),
    Person(Character.George.name, R.drawable.george_png),
    Person(Character.Kramer.name, R.drawable.kramer_png),
)

sealed class Character(val name: String) {
    object Jerry: Character("Jerry")
    object George: Character("George")
    object Elaine: Character("Elaine")
    object Kramer: Character("Kramer")
}