package com.cumpatomas.seinfeldrecords.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cumpatomas.seinfeldrecords.data.model.QuestionModel

@Entity(tableName = "question_entity")
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val question: String,
    val answer: String,
    var answered: Boolean = false
)

fun QuestionEntity.toModel() = QuestionModel(
    question = question,
    answer = answer,
    answered = answered
)
