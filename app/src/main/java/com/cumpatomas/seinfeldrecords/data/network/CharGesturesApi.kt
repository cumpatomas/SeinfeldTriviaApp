package com.cumpatomas.seinfeldrecords.data.network

import com.cumpatomas.seinfeldrecords.data.model.CharGestures
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface CharGesturesApi {
    @GET
    suspend fun getCharGestures(
        @Url url: String
    ): Response<List<CharGestures>>
}