package com.cumpatomas.seinfeldrecords.data.network

import com.cumpatomas.seinfeldrecords.data.database.QuestionDao
import com.cumpatomas.seinfeldrecords.data.database.entities.toModel
import com.cumpatomas.seinfeldrecords.data.model.QuestionModel
import com.cumpatomas.seinfeldrecords.data.model.toEntity
import javax.inject.Inject

class QuestionService @Inject constructor(
    private val retrofit: QuestionsAPI,
    private val questionDao: QuestionDao
) {

    suspend fun getQuestions(): ResponseEvent<List<QuestionModel>> {
        return try {
            val localQuestionList = questionDao.getQuestionsList().map { it.toModel().question }
            val response = retrofit.getQuestions("questions/seinfeld_questions.json")
            if (response.isSuccessful) {
                response.body()?.let { questionsList ->
                    for (question in questionsList) {
                        if (question.question !in localQuestionList)
                        questionDao.insertQuestion(question.toEntity())
                    }
                    ResponseEvent.Success(questionsList)
                } ?: run {
                    ResponseEvent.Error(Exception("Response body is null."))
                }
            } else {
                ResponseEvent.Error(Exception("Get questions not succesfull."))
            }
        } catch (e: Exception) {
            ResponseEvent.Error(e)
        }
    }

}