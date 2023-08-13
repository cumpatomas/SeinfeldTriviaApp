package com.cumpatomas.seinfeldrecords.ui.chargestures

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cumpatomas.seinfeldrecords.data.model.CharGestures
import com.cumpatomas.seinfeldrecords.domain.GetCharGestures
import com.cumpatomas.seinfeldrecords.domain.GetUserPoints
import com.cumpatomas.seinfeldrecords.domain.InsertGesturesInDataBase
import com.cumpatomas.seinfeldrecords.domain.MAX_POINTS
import com.cumpatomas.seinfeldrecords.domain.SaveUserPoints
import com.cumpatomas.seinfeldrecords.domain.SetCharGestureAsCompleted
import com.cumpatomas.seinfeldrecords.domain.ZERO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
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
    private val _randomGestureId = MutableStateFlow("")
    val randomGestureId = _randomGestureId.asStateFlow()
    private val _userPoints = MutableStateFlow<Int>(0)
    val userPoints = _userPoints.asStateFlow()
    private val _questionsCorrect = MutableStateFlow(0)
    val questionsCorrect = _questionsCorrect.asStateFlow()
    private val _playing = MutableStateFlow(false)
    val playing = _playing.asStateFlow()

    init {
        viewModelScope.launch {
            _userPoints.value = getPoints.invoke()
            insertGesturesInDataBase.invoke()
        }
    }

    fun getCharSelected(char: String) {
        viewModelScope.launch(IO) {
            _loading.value = true
            delay(2000)
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

    fun setPoints(points: Int) {
        viewModelScope.launch() {
            if ((_userPoints.value + points) in ZERO..MAX_POINTS) {
                _userPoints.value += points
                launch {
                    updatePoints.invoke(_userPoints.value)
                }.join()
                getPoints.invoke()
            } else {
                if (_userPoints.value >= MAX_POINTS) {
                    _userPoints.value = MAX_POINTS
                    updatePoints.invoke(_userPoints.value)
                } else {
                    _userPoints.value = ZERO
                    updatePoints.invoke(_userPoints.value)
                }
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

    fun playAudio(url: String) {
        val mediaPlayer: MediaPlayer = MediaPlayer()
        mediaPlayer.setAudioAttributes(
            AudioAttributes
                .Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )

        try {
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepare()
            //mp3 will be started after completion of preparing...
            mediaPlayer.setOnPreparedListener { player ->
                player.start()
                _playing.value = true
                mediaPlayer.setOnCompletionListener {
                    _playing.value = false
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            _playing.value = false
        }
    }

    fun playShortAudio(url: String) {
        val mediaPlayer: MediaPlayer = MediaPlayer()

        mediaPlayer.setAudioAttributes(
            AudioAttributes
                .Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )

        try {
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepare()
            //mp3 will be started after completion of preparing...
            mediaPlayer.setOnPreparedListener { player ->
                player.start()
            }
            mediaPlayer.setOnCompletionListener {

            }
        } catch (e: IOException) {
            e.printStackTrace()
            _playing.value = false
        }
    }
}