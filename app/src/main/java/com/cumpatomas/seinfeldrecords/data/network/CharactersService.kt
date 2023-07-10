package com.cumpatomas.seinfeldrecords.data.network

import com.cumpatomas.seinfeldrecords.data.model.SeinfeldChar
import javax.inject.Inject

class CharactersService @Inject constructor(private val retrofit: CharListApi) {
    suspend fun getCharacters(): ResponseEvent<List<SeinfeldChar>> {
        return try {

            val response = retrofit.getCharList("characters/characters")
            if (response.isSuccessful) {
                response.body()?.let { charList ->
                    ResponseEvent.Success(charList)
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