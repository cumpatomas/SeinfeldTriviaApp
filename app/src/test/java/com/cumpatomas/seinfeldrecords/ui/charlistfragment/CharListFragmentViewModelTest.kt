package com.cumpatomas.seinfeldrecords.ui.charlistfragment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.cumpatomas.seinfeldrecords.data.database.GestureDao
import com.cumpatomas.seinfeldrecords.data.database.QuestionDao
import com.cumpatomas.seinfeldrecords.data.database.entities.GestureEntity
import com.cumpatomas.seinfeldrecords.data.model.QuestionModel
import com.cumpatomas.seinfeldrecords.data.model.SeinfeldChar
import com.cumpatomas.seinfeldrecords.data.network.QuestionService
import com.cumpatomas.seinfeldrecords.data.network.ResponseEvent
import com.cumpatomas.seinfeldrecords.domain.GetCharListUseCase
import com.cumpatomas.seinfeldrecords.domain.GetUserPoints
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CharListFragmentViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private val dispatchers = StandardTestDispatcher()
    private lateinit var viewModel: CharListFragmentViewModel
    private val charProvider: GetCharListUseCase = mockk()
    private val gesturesProvider: GestureDao = mockk(relaxed = true)
    private val pointsProvider: GetUserPoints = mockk()
    private val questionService: QuestionService = mockk()
    private val questionDao: QuestionDao = mockk(relaxed = true)
    private val charFakeList = listOf<SeinfeldChar>(
        SeinfeldChar(
            name = "Brandon Barr",
            shortName = "Ervin Burt",
            specs = "nam",
            relationWithJerry = "voluptatum",
            photo = "a",
            completed = false
        ),
        SeinfeldChar(
            name = "Guadalupe Hodge",
            shortName = "Casey Smith",
            specs = "reprimique",
            relationWithJerry = "fringilla",
            photo = "invenire",
            completed = true
        ),
        SeinfeldChar(
            name = "Laurel French",
            shortName = "Jeanie Cardenas",
            specs = "natum",
            relationWithJerry = "metus",
            photo = "theophrastus",
            completed = false
        )
    )
    private val questionsFakeList = listOf<QuestionModel>(
        QuestionModel(question = "hac", answer = "tortor", answered = false),
        QuestionModel(question = "oratio", answer = "magnis", answered = false),
        QuestionModel(question = "fabulas", answer = "discere", answered = false)
    )
    private val gesturesFakeList = listOf<GestureEntity>(
        GestureEntity(charName = "Aldo Norman", completed = false),
        GestureEntity(charName = "Earline Vazquez", completed = true),
        GestureEntity(charName = "Pepe Lopez", completed = true)
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatchers)
        coEvery { pointsProvider.invoke() } returns 10
        coEvery { charProvider.invoke() } returns charFakeList
        coEvery { questionService.getQuestions() } returns ResponseEvent.Success(questionsFakeList)
        coEvery { gesturesProvider.getGestureList() } returns gesturesFakeList

        viewModel = CharListFragmentViewModel(
            charProvider,
            gesturesProvider,
            pointsProvider,
            questionService,
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `check viewmodel init method`() = runTest {
        viewModel.userPoints.test {
            val points = awaitItem()
            assertThat(points == 10).isTrue()
        }
    }

    /** questionService.getQuestions() method must be checked as instrumented test to check
     * if the list was saved correctly into the DB
     */

}