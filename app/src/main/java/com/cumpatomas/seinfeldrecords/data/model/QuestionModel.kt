package com.cumpatomas.seinfeldrecords.data.model

import com.cumpatomas.seinfeldrecords.data.database.entities.QuestionEntity

data class QuestionModel(
    val question: String,
    val answer: String,
    var answered: Boolean = false,
)

fun QuestionModel.toEntity() = QuestionEntity(
    question = question,
    answer = answer,
    answered = answered
)