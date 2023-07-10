package com.cumpatomas.seinfeldrecords.domain

import android.util.Log
import com.cumpatomas.seinfeldrecords.data.model.CharGestures
import com.cumpatomas.seinfeldrecords.data.network.GesturesService
import com.cumpatomas.seinfeldrecords.data.network.ResponseEvent
import javax.inject.Inject

class GetCharGestures @Inject constructor(
    private val provider: GesturesService
) {
    suspend operator fun invoke(selectedChar: String): List<CharGestures> {
        return when (val result = provider.getGestures()) {
            is ResponseEvent.Success -> {
                println(result.data.filter { it.char == selectedChar })
                result.data.filter { it.char == selectedChar }

            }

            is ResponseEvent.Error -> {
                Log.d("Exception", result.exception.stackTraceToString())
                emptyList()
            }
        }
    }
}

