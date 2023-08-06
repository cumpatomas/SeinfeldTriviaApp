package com.cumpatomas.seinfeldrecords.domain

import android.util.Log
import com.cumpatomas.seinfeldrecords.data.model.SeinfeldChar
import com.cumpatomas.seinfeldrecords.data.network.CharactersService
import com.cumpatomas.seinfeldrecords.data.network.ResponseEvent
import javax.inject.Inject

class GetCharListUseCase @Inject constructor(
    private val charProvider: CharactersService
) {
    suspend operator fun invoke(): List<SeinfeldChar> {
        return when (val result = charProvider.getCharacters()) {
            is ResponseEvent.Success -> {
                result.data
            }

            is ResponseEvent.Error -> {
                Log.d("Exception", result.exception.stackTraceToString())
                emptyList()
            }
        }
    }
}