package com.cumpatomas.seinfeldrecords.ui.quizFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cumpatomas.seinfeldrecords.data.database.QuestionDao
import com.cumpatomas.seinfeldrecords.data.database.entities.toModel
import com.cumpatomas.seinfeldrecords.data.model.QuestionModel
import com.cumpatomas.seinfeldrecords.domain.GetUserAdsState
import com.cumpatomas.seinfeldrecords.domain.GetUserPoints
import com.cumpatomas.seinfeldrecords.domain.MAX_POINTS
import com.cumpatomas.seinfeldrecords.domain.SaveUserPoints
import com.cumpatomas.seinfeldrecords.domain.UpdateAnsweredQuestion
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
    private val questionDao: QuestionDao,
    private val updateAnsweredQuestion: UpdateAnsweredQuestion,
    private val getUserAdsState: GetUserAdsState,
) : ViewModel(
) {
    private val _questionsList = MutableStateFlow(emptyList<QuestionModel>())
    private val _correctAnswer = MutableStateFlow("")
    val correctAnswer = _correctAnswer.asStateFlow()
    private val _randomQuestion = MutableStateFlow("")
    val randomQuestion = _randomQuestion.asStateFlow()
    private val _userPoints = MutableStateFlow(0)
    val userPoints = _userPoints.asStateFlow()
    var questionAnswered = false
    var answerIsCorrect = false
    var randomResponseText = ""
    private val _questionsCorrect = MutableStateFlow(0)
    val questionsCorrect = _questionsCorrect.asStateFlow()
    private val _totalQuestions = MutableStateFlow(0)
    val totalQuestions = _totalQuestions.asStateFlow()
    private var localQuestionList = emptyList<QuestionModel>()
    private val _noAdsState = MutableStateFlow(false)
    val noAdsState = _noAdsState.asStateFlow()

    init {
        viewModelScope.launch {
            _userPoints.value = getPoints.invoke()
            localQuestionList = questionDao.getQuestionsList().map { it.toModel() }
            _questionsCorrect.value = questionDao.getQuestionsList().filter { it.answered }.size
            val notAnsweredList = localQuestionList.filter { !it.answered }
            launch {
                _questionsList.value = notAnsweredList
                val random = (0..notAnsweredList.lastIndex).shuffled().random()
                _randomQuestion.value = notAnsweredList[random].question
                _correctAnswer.value = notAnsweredList[random].answer
            }.join()
            _totalQuestions.value = localQuestionList.size
            _noAdsState.value = getUserAdsState.invoke()
        }
    }

    fun getNewQuestion() {
        viewModelScope.launch(IO) {
            val random = (0..localQuestionList.lastIndex).shuffled().random()
            _randomQuestion.value = localQuestionList[random].question
            _correctAnswer.value = localQuestionList[random].answer
            _noAdsState.value = getUserAdsState.invoke()
        }
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
                }
                getPoints.invoke()
            } else {
                if (_userPoints.value + points >= MAX_POINTS) {
                    launch {
                        _userPoints.value = MAX_POINTS
                        updatePoints.invoke(_userPoints.value)
                    }

                } else {
                    launch {
                        _userPoints.value = ZERO
                        updatePoints.invoke(_userPoints.value)

                    }
                }
            }
        }
    }

    fun updateAnswerToTrue() {
        viewModelScope.launch() {
            launch {
                updateAnsweredQuestion.invoke(_randomQuestion.value, true)
            }.join()
            localQuestionList =
                questionDao.getQuestionsList().map { it.toModel() }.filter { !it.answered }
            _questionsCorrect.value++
        }
    }
}

