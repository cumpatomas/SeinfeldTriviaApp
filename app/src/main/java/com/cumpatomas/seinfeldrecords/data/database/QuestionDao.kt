package com.cumpatomas.seinfeldrecords.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.cumpatomas.seinfeldrecords.data.database.entities.QuestionEntity

@Dao
interface QuestionDao {
    @Insert
    suspend fun insertQuestion(question: QuestionEntity)

    @Query("SELECT * FROM question_entity")
    suspend fun getQuestionsList(): List<QuestionEntity>

    @Query("UPDATE question_entity SET answered = :answered WHERE question = :question")
    suspend fun updateQuestion(question: String, answered: Boolean)
}