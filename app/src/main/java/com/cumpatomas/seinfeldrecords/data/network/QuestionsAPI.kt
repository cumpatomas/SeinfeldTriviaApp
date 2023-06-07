package com.cumpatomas.seinfeldrecords.data.network

import com.cumpatomas.seinfeldrecords.data.model.QuestionModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface QuestionsAPI {

    @GET
    suspend fun getQuestions(
        @Url url: String
    ): Response<List<QuestionModel>>
}