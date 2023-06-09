package com.cumpatomas.seinfeldrecords.data.network

import com.cumpatomas.seinfeldrecords.data.model.QuestionModel
import javax.inject.Inject

class QuestionService @Inject constructor(private val retrofit: QuestionsAPI ) {

    suspend fun getQuestions(): ResponseEvent<List<QuestionModel>> {
        return try {
            val response = retrofit.getQuestions("questions/seinfeld_questions")
            if (response.isSuccessful) {
                response.body()?.let { questionsList ->
                    ResponseEvent.Success(questionsList)
                } ?: run {
                    ResponseEvent.Error(Exception("Response body is null."))
                }
            } else {
                ResponseEvent.Error(Exception("Get recipes not succesfull."))
            }
        } catch (e: Exception) {
            ResponseEvent.Error(e)
        }
    }

}