package com.cumpatomas.seinfeldrecords.ui.homefragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cumpatomas.seinfeldrecords.core.ex.addSpaces
import com.cumpatomas.seinfeldrecords.data.network.QuestionService
import com.cumpatomas.seinfeldrecords.data.network.ResponseEvent
import com.cumpatomas.seinfeldrecords.domain.GetRandomScript
import com.cumpatomas.seinfeldrecords.domain.GetUserPoints
import com.cumpatomas.seinfeldrecords.domain.MAX_POINTS
import com.cumpatomas.seinfeldrecords.domain.SaveUserPoints
import com.cumpatomas.seinfeldrecords.domain.ScrapScripts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.stream.Collectors.counting
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val scraping : ScrapScripts,
    private val script: GetRandomScript,
    val getPoints: GetUserPoints,
    private val updatePoints: SaveUserPoints,
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
    private val _userPoints = MutableStateFlow<Int>(0)
    val userPoints = _userPoints.asStateFlow()

    init {
        viewModelScope.launch() {
            _userPoints.value = getPoints.invoke()
            getNewScript()
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
            println("randomLines: ")
            println(randomLines)
            _list.value = emptyList()
            for (i in randomLines) {
                _list.value += i
            }
            println(_list.value)
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
            println("titulos")
            println(_titlesList.value)
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

    fun getPoints() {

        viewModelScope.launch() {
            _userPoints.value = getPoints.invoke()
        }
    }
}
