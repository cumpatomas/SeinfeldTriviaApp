package com.cumpatomas.seinfeldrecords.data.model

data class CharGestures(
    val id: String,
    val char: String,
    val audioLink: String,
    val photoLink: String,
    var clicked: Boolean = false,
    var completed: Boolean = false
)