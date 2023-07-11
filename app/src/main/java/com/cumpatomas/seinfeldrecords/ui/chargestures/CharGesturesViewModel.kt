package com.cumpatomas.seinfeldrecords.ui.chargestures

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cumpatomas.seinfeldrecords.data.model.CharGestures
import com.cumpatomas.seinfeldrecords.domain.GetCharGestures
import com.cumpatomas.seinfeldrecords.domain.GetUserPoints
import com.cumpatomas.seinfeldrecords.domain.InsertGesturesInDataBase
import com.cumpatomas.seinfeldrecords.domain.MAX_POINTS
import com.cumpatomas.seinfeldrecords.domain.SaveUserPoints
import com.cumpatomas.seinfeldrecords.domain.SetCharGestureAsCompleted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharGesturesViewModel @Inject constructor(
    private val provider: GetCharGestures,
    private val getPoints: GetUserPoints,
    private val updatePoints: SaveUserPoints,
    private val insertGesturesInDataBase: InsertGesturesInDataBase,
    private val charCompleted: SetCharGestureAsCompleted,
) : ViewModel() {
    private val _gesturesList = MutableStateFlow(listOf<CharGestures>())
    val gesturesList = _gesturesList.asStateFlow()
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()
    private val _buttonIsPlaying = MutableStateFlow(false)
    val buttonIsPlaying = _buttonIsPlaying.asStateFlow()
    private val _randomGestureId = MutableStateFlow("")
    val randomGestureId = _randomGestureId.asStateFlow()
    private val _userPoints = MutableStateFlow<Int>(0)
    val userPoints = _userPoints.asStateFlow()
    private val _questionsCorrect = MutableStateFlow(0)
    val questionsCorrect = _questionsCorrect.asStateFlow()

    init {
        viewModelScope.launch {
            _userPoints.value = getPoints.invoke()
            insertGesturesInDataBase.invoke()
        }
    }

    fun getCharSelected(char: String) {
        viewModelScope.launch(IO) {
            _loading.value = true
            launch {
                _gesturesList.value = provider.invoke(char)
            }.join()
            _randomGestureId.value = _gesturesList.value.shuffled().random().id
            _loading.value = false
        }
    }

    fun getRandomId() {
        if (_gesturesList.value.filter { !it.clicked }.isNotEmpty())
            _randomGestureId.value =
                _gesturesList.value.filter { !it.clicked }.shuffled().random().id
    }

    fun buttonPlay(boolean: Boolean) {
        _buttonIsPlaying.value = boolean
    }

    fun setPoints(points: Int) {
        viewModelScope.launch() {
            if ((_userPoints.value + points) <= MAX_POINTS || points < 0) {
                _userPoints.value += points
                launch {
                    updatePoints.invoke(_userPoints.value)
                    getPoints.invoke()
                }.join()

                if (_userPoints.value < 0) {
                    _userPoints.value = 0
                }
            } else {
                _userPoints.value = MAX_POINTS
            }
        }
    }

    fun countQuestion() {
        _questionsCorrect.value++
    }

    fun resetQuestion() {
        _questionsCorrect.value = 0
    }

    fun setCharScreenComplete(selectedChar: String) {
        viewModelScope.launch(IO) {
            charCompleted.invoke(selectedChar)
        }
    }
}