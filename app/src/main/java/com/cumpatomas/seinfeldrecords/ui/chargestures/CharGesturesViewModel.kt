package com.cumpatomas.seinfeldrecords.ui.chargestures

import androidx.lifecycle.ViewModel
import com.cumpatomas.seinfeldrecords.data.CharGesturesProvider
import com.cumpatomas.seinfeldrecords.data.model.CharGestures
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CharGesturesViewModel : ViewModel() {
    private val provider = CharGesturesProvider()
    private val _gesturesList = MutableStateFlow(listOf<CharGestures>())
    val gesturesList = _gesturesList.asStateFlow()
    private val _buttonIsPlaying = MutableStateFlow(false)
    val buttonIsPlaying = _buttonIsPlaying.asStateFlow()

    init {
        _gesturesList.value = provider.invoke("Estelle")
    }

    fun buttonPlay(boolean: Boolean) {
        _buttonIsPlaying.value = boolean
    }
}