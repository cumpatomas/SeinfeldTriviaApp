package com.cumpatomas.seinfeldrecords.ui.homefragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cumpatomas.seinfeldrecords.core.ex.addSpaces
import com.cumpatomas.seinfeldrecords.domain.GetRandomScript
import com.cumpatomas.seinfeldrecords.domain.GetUserAdsState
import com.cumpatomas.seinfeldrecords.domain.GetUserPoints
import com.cumpatomas.seinfeldrecords.domain.MAX_POINTS
import com.cumpatomas.seinfeldrecords.domain.SaveUserPoints
import com.cumpatomas.seinfeldrecords.domain.ScrapScripts
import com.cumpatomas.seinfeldrecords.domain.ZERO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val scraping: ScrapScripts,
    private val script: GetRandomScript,
    val getPoints: GetUserPoints,
    private val updatePoints: SaveUserPoints,
    private val getUserAdsState: GetUserAdsState,
) : ViewModel() {
    private val _list = MutableStateFlow<List<String>>(emptyList())
    val list = _list.asStateFlow()
    private var urls = emptyList<String>()
    private val _titlesList = MutableStateFlow<MutableList<String>>(mutableListOf())
    val titlesList = _titlesList.asStateFlow()
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()
    private val _timer = MutableStateFlow(15)
    val timer = _timer.asStateFlow()
    var timerOn = true
    var gifActive = false
    var correctAnswer = false
    var timeOut = false
    private val _userPoints = MutableStateFlow(0)
    val userPoints = _userPoints.asStateFlow()
    private val _nextButtonPressedTimes = MutableStateFlow(0)
    val nextButtonPressedTimes = _nextButtonPressedTimes.asStateFlow()
    private val _noAdsState = MutableStateFlow(false)
    val noAdsState = _noAdsState.asStateFlow()

    init {
        viewModelScope.launch(IO) {
            _userPoints.value = getPoints.invoke()
            getNewScript()
            _noAdsState.value = getUserAdsState.invoke()
        }
    }

    suspend fun getNewScript() {
        var randomLines = listOf<String>()
        _loading.value = true
        viewModelScope.launch(IO) {
            launch {
                urls = scraping.invoke()
            }.join()
            launch {
                randomLines = script.invoke(urls.shuffled().random())
            }.join()
            _list.value = emptyList()
            for (i in randomLines) {
                _list.value += i
            }
            counting()
            _titlesList.value = emptyList<String>().toMutableList()
            _titlesList.value =
                urls.map { it.substringAfterLast("/").substringBefore(".") }.toMutableList()
            val tempList = mutableListOf<String>()
            val tempList2 = _titlesList.value.toMutableList()
            for (title in tempList2) {
                tempList += title.addSpaces()
            }
            _titlesList.value = tempList.shuffled().take(3).toMutableList()
            _noAdsState.value = getUserAdsState.invoke()
        }
    }

    fun resetCounter() {
        _timer.value = 15
        viewModelScope.launch {
            timerOn = false
            delay(1000)
            timerOn = true
        }
    }

    var countingJob: Job? = null
    private fun counting() {
        var count = 15
        _timer.value = 15
        countingJob = viewModelScope.launch {
            while (count != 0) {
                delay(1000)
                count--
                _timer.value = count

                if (!timerOn) {
                    _timer.value = 15
                    cancel()
                }
            }
        }
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
                if (_userPoints.value + points >= MAX_POINTS) {
                    _userPoints.value = MAX_POINTS
                    updatePoints.invoke(_userPoints.value)
                } else {
                    _userPoints.value = ZERO
                    updatePoints.invoke(_userPoints.value)
                }
            }
        }
    }

    fun getPoints() {
        viewModelScope.launch() {
            _userPoints.value = getPoints.invoke()
        }
    }

    fun countNextButtonPressed() {
        _nextButtonPressedTimes.value++
    }
}
