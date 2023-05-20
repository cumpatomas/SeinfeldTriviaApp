package com.cumpatomas.seinfeldrecords.data.model

import com.cumpatomas.seinfeldrecords.data.database.entities.RecordEntity

data class CharRecord(
    val id: Int? = null,
    var name: String? = null,
    val mainChar: String? = null,
    val description: String? = null,
    val videoid: String? = null,
    val preview: String? = null
    )

fun RecordEntity.toDomain() = CharRecord(
    id = id ?: 0,
    name = name,
    mainChar = mainChar,
    description = description,
    videoid = videoid,
    preview = preview
)
