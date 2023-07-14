package com.cumpatomas.seinfeldrecords.ui.quizFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cumpatomas.seinfeldrecords.data.model.QuestionModel
import com.cumpatomas.seinfeldrecords.data.network.QuestionService
import com.cumpatomas.seinfeldrecords.data.network.ResponseEvent
import com.cumpatomas.seinfeldrecords.domain.GetUserPoints
import com.cumpatomas.seinfeldrecords.domain.MAX_POINTS
import com.cumpatomas.seinfeldrecords.domain.SaveUserPoints
import com.cumpatomas.seinfeldrecords.domain.ZERO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizFragmentViewModel @Inject constructor(
    private val getPoints: GetUserPoints,
    private val updatePoints: SaveUserPoints,
    private val getQuestions: QuestionService
) : ViewModel(
) {
    private val _questionsList = MutableStateFlow(emptyList<QuestionModel>())
    private val _correctAnswer = MutableStateFlow<String>("")
    val correctAnswer = _correctAnswer.asStateFlow()
    private val _randomQuestion = MutableStateFlow("")
    val randomQuestion = _randomQuestion.asStateFlow()
    private val _userPoints = MutableStateFlow<Int>(0)
    val userPoints = _userPoints.asStateFlow()
    var questionAnswered = false
    var answerIsCorrect = false
    var randomResponseText = ""
    private val _questionCounting = MutableStateFlow(0)
    val questionCounting = _questionCounting.asStateFlow()
    private val _questionsCorrect = MutableStateFlow(0)
    val questionsCorrect = _questionsCorrect.asStateFlow()

    init {
        viewModelScope.launch {
            _userPoints.value = getPoints.invoke()

            launch {
                when (val questions = getQuestions.getQuestions()) {
                    is ResponseEvent.Error -> {
                        questions.exception
                    }

                    is ResponseEvent.Success -> {
                        _questionsList.value = questions.data
                        val random = (0..questions.data.lastIndex).shuffled().random()
                        _randomQuestion.value = questions.data[random].question
                        _correctAnswer.value = questions.data[random].answer
                    }
                }
            }.join()
        }
    }

    fun getNewQuestion() {
        val random = (0.._questionsList.value.lastIndex).shuffled().random()
        _randomQuestion.value = _questionsList.value[random].question
        _correctAnswer.value = _questionsList.value[random].answer
    }

    fun getPoints() {
        viewModelScope.launch(IO) {
            while (true) {
                _userPoints.value = getPoints.invoke()
                delay(2000)
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
                if (_userPoints.value >= MAX_POINTS) {
                    _userPoints.value = MAX_POINTS
                } else {
                    _userPoints.value = ZERO
                }
            }
        }
    }

    fun countQuestion() {
        _questionCounting.value++
    }
}