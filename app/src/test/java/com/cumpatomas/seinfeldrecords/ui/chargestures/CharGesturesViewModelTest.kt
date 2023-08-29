package com.cumpatomas.seinfeldrecords.ui.chargestures

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.cumpatomas.seinfeldrecords.data.model.CharGestures
import com.cumpatomas.seinfeldrecords.domain.GetCharGestures
import com.cumpatomas.seinfeldrecords.domain.GetUserPoints
import com.cumpatomas.seinfeldrecords.domain.InsertGesturesInDataBase
import com.cumpatomas.seinfeldrecords.domain.SaveUserPoints
import com.cumpatomas.seinfeldrecords.domain.SetCharGestureAsCompleted
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CharGesturesViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private val dispatchers = StandardTestDispatcher()
    lateinit var viewModel: CharGesturesViewModel
    private val provider: GetCharGestures = mockk()
    private val getPoints: GetUserPoints = mockk(relaxed = true)
    private val updatePoints: SaveUserPoints = mockk(relaxed = true)
    private val insertGesturesInDataBase: InsertGesturesInDataBase = mockk(relaxed = true)
    private val charCompleted: SetCharGestureAsCompleted = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatchers)

        viewModel = CharGesturesViewModel(
            provider,
            getPoints,
            updatePoints,
            insertGesturesInDataBase,
            charCompleted
        )
        val fakeGesturesList = listOf<CharGestures>(
            CharGestures(
                id = "tortor",
                char = "tempus",
                audioLink = "consetetur",
                photoLink = "sagittis",
                clicked = false,
                completed = false
            ),
            CharGestures(
                id = "indoctum",
                char = "duo",
                audioLink = "fuisset",
                photoLink = "condimentum",
                clicked = true,
                completed = true
            ),
            CharGestures(
                id = "patrioque",
                char = "tractatos",
                audioLink = "graeco",
                photoLink = "adolescens",
                clicked = false,
                completed = false
            )
        )

        coEvery { provider.invoke(any()) } returns fakeGesturesList
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `check setPoints function updates userpoints value`() = runTest {
        withContext(Dispatchers.Default) {
            viewModel.userPoints.test() {
                var result = awaitItem()
                assertThat(result == 0).isTrue()
                viewModel.setPoints(2)
                result = awaitItem()
                assertThat(result == 2).isTrue()
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `check gesturesList received on getCharSelected function`() = runTest {
        withContext(Dispatchers.IO) {
            viewModel.gesturesList.test(10000) { // set the timeout because inside the getCharSelected function there's a delay of 2000 ms
                var result = awaitItem()
                assertThat(result.isEmpty()).isTrue()
                viewModel.getCharSelected("tempus")
                result = awaitItem()
                assertThat(result.isNotEmpty()).isTrue()
                /** we check getRandomId function here to have a value for gesturesList*/
                viewModel.randomGestureId.test() {
                    var result2 = awaitItem()
                    assertThat(result2.isNotEmpty()).isTrue()
                    viewModel.getRandomId()
                    advanceUntilIdle()
                    result2 = awaitItem()
                    assertThat(result2 == "tortor" || result2 == "patrioque").isTrue()
                }
            }
        }
    }

    @Test
    fun `fun countQuestions and resetQuestions check`() = runTest {
        viewModel.questionsCorrect.test {
            var result = awaitItem()
            assertThat(result == 0).isTrue()
            viewModel.countQuestion()
            result = awaitItem()
            assertThat(result == 1).isTrue()
            viewModel.resetQuestion()
            result = awaitItem()
            assertThat(result == 0).isTrue()
        }
    }
}