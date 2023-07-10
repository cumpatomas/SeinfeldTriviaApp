package com.cumpatomas.seinfeldrecords.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cumpatomas.seinfeldrecords.data.model.CharGestures

@Entity(tableName = "gesture_entity")
data class GestureEntity(
    @PrimaryKey val charName: String,
    var completed: Boolean = false
)

fun GestureEntity.toModel(): CharGestures {
    return CharGestures(
        id = "",
        char = charName,
        audioLink = "",
        photoLink = "",
        clicked = false,
        completed = completed
    )
}

