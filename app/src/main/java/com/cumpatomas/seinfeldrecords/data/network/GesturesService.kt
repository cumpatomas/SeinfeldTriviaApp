package com.cumpatomas.seinfeldrecords.data.network

import com.cumpatomas.seinfeldrecords.data.model.CharGestures
import javax.inject.Inject

class GesturesService @Inject constructor(
    private val retrofit: CharGesturesApi
) {
    suspend fun getGestures(): ResponseEvent<List<CharGestures>> {
        return try {
            val response = retrofit.getCharGestures("audios/chargestures.json")
            if (response.isSuccessful) {
                response.body()?.let { gesturesList ->
                    ResponseEvent.Success(gesturesList)
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