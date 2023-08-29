package com.cumpatomas.seinfeldrecords.ui.quizFragment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.cumpatomas.seinfeldrecords.data.database.QuestionDao
import com.cumpatomas.seinfeldrecords.data.model.QuestionModel
import com.cumpatomas.seinfeldrecords.data.model.toEntity
import com.cumpatomas.seinfeldrecords.domain.GetUserAdsState
import com.cumpatomas.seinfeldrecords.domain.GetUserPoints
import com.cumpatomas.seinfeldrecords.domain.SaveUserPoints
import com.cumpatomas.seinfeldrecords.domain.UpdateAnsweredQuestion
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class QuizFragmentViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private val dispatchers = StandardTestDispatcher()
    private val getPoints: GetUserPoints = mockk()
    private val updatePoints: SaveUserPoints = mockk(relaxed = true)
    private val questionDao: QuestionDao = mockk()
    private val updateAnsweredQuestion: UpdateAnsweredQuestion = mockk(relaxed = true)
    private val getUserAdsState: GetUserAdsState = mockk(relaxed = true)
    private lateinit var viewModel: QuizFragmentViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatchers)
        viewModel = QuizFragmentViewModel(
            getPoints,
            updatePoints,
            questionDao,
            updateAnsweredQuestion,
            getUserAdsState
        )
        val questionList = listOf<QuestionModel>(
            QuestionModel(
                question = "cursus",
                answer = "suscipit",
                answered = false
            ),
            QuestionModel(
                question = "fabellas",
                answer = "fames",
                answered = true
            )
        )

        coEvery { questionDao.getQuestionsList() } returns questionList.map { it.toEntity() }
        coEvery { getPoints.invoke() } returns 10
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `check questionsCorrect value after init`() = runTest {
        viewModel.questionsCorrect.test() {
            val result = awaitItem()
            assertThat(result == 1).isTrue()
        }
    }

    @Test
    fun `check getPoints function sets userpoints value`() = runBlocking {
        viewModel.userPoints.test {
            var result = awaitItem()
            assertThat(result == 0).isTrue()
            viewModel.getPoints()
            result = awaitItem()
            assertThat(result == 10).isTrue()
        }
    }

    @Test
    fun `check setPoints function updates userpoints value`() = runTest {
        withContext(Dispatchers.Default) {
            viewModel.userPoints.test() {
                var result = awaitItem()
                assertThat(result == 10).isTrue()
                viewModel.setPoints(2)
                result = awaitItem()
                assertThat(result == 12).isTrue()
                viewModel.setPoints(-13)
                result = awaitItem()
                assertThat(result == 0).isTrue()
                viewModel.setPoints(1001)
                result = awaitItem()
                assertThat(result == 1000).isTrue()
                cancel()
            }
        }
    }
    @Test
    fun `check questionsCorrect value updates in updateAnswerToTrue function`() = runTest {
        viewModel.questionsCorrect.test {
            var result = awaitItem()
            assertThat(result == 1).isTrue() // it takes the 1 answered question from the fake questionList
            viewModel.updateAnswerToTrue()
            result = awaitItem()
            assertThat(result == 2).isTrue()
        }
    }
}