package com.cumpatomas.seinfeldrecords.data.model

import com.google.gson.annotations.SerializedName

data class SeinfeldChar(
    val name: String,
    @SerializedName("shortName")
    val shortName: String,
    @SerializedName("description")
    val specs: String,
    @SerializedName("details")
    val relationWithJerry: String,
    @SerializedName("imageURL")
    val photo: String,
    var completed: Boolean = false
)