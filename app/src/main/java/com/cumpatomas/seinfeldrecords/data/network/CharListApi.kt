package com.cumpatomas.seinfeldrecords.data.network

import com.cumpatomas.seinfeldrecords.data.model.SeinfeldChar
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface CharListApi {
    @GET
    suspend fun getCharList(
        @Url url: String
    ): Response<List<SeinfeldChar>>
}