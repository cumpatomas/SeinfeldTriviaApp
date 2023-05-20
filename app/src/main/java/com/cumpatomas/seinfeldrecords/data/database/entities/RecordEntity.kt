package com.cumpatomas.seinfeldrecords.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cumpatomas.seinfeldrecords.data.model.CharRecord

@Entity(tableName = "record_entity")
data class RecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    var name: String?,
    val mainChar: String?,
    val description: String?,
    val videoid: String?,
    val preview: String?
)

fun CharRecord.toEntity() = RecordEntity(
    name = name,
    mainChar =  mainChar,
    description = description,
    videoid = videoid,
    preview = preview


)