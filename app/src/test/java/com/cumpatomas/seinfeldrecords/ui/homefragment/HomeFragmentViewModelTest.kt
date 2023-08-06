package com.cumpatomas.seinfeldrecords.ui.homefragment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.cumpatomas.seinfeldrecords.domain.GetRandomScript
import com.cumpatomas.seinfeldrecords.domain.GetUserAdsState
import com.cumpatomas.seinfeldrecords.domain.GetUserPoints
import com.cumpatomas.seinfeldrecords.domain.SaveUserPoints
import com.cumpatomas.seinfeldrecords.domain.ScrapScripts
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
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

class HomeFragmentViewModelTest() {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private val dispatchers = StandardTestDispatcher()
    private lateinit var viewModel: HomeFragmentViewModel

    private val scraping: ScrapScripts = mockk()
    private val randomScript: GetRandomScript = mockk()
    private val getPoints: GetUserPoints = mockk()
    private val updatePoints: SaveUserPoints = mockk(relaxed = true) // mocks the savepoints function but as it is relaxed we don't have to mock the data base
    private val listTest = mutableListOf("uno", "dos", "tres")
    private val getUserAdsState: GetUserAdsState = mockk(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatchers)
        coEvery { randomScript.invoke(any()) } returns listTest
        coEvery { scraping.invoke() } returns listTest
        coEvery { getPoints.invoke() } returns 10
        viewModel = HomeFragmentViewModel(
            scraping,
            randomScript,
            getPoints,
            updatePoints,
            getUserAdsState
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `get points and get news script at init`() = runTest {
        viewModel.userPoints.test {
            val result = awaitItem()
            assertThat(result == 10).isTrue()
        }
        viewModel.titlesList.test {
            val result = awaitItem()
            assertThat(result.isNotEmpty()).isTrue()
        }
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun `reset counter test`() = runTest {
        viewModel.timer.test(timeout = 15.seconds) {
            val actualTime = awaitItem()
            assertThat(actualTime == 15).isTrue()
            Thread.sleep(1000) // apparently delay() function doesn't work because it delays the same dispatcher thread¿?
            assertThat(awaitItem() == 14).isTrue()
            viewModel.resetCounter()
            assertThat(awaitItem() == 15).isTrue()
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `setPoints function test and minimum (zero) and max (1000) points accepted`() = runTest {
        viewModel.userPoints.test {
            var actualPoints = awaitItem()
            assertThat(actualPoints == 10).isTrue()
            viewModel.setPoints(2)
            actualPoints = awaitItem()
            assertThat(actualPoints == 12).isTrue()
            viewModel.setPoints(1200)
            actualPoints = awaitItem()
            assertThat(actualPoints == 1000).isTrue()
            viewModel.setPoints(-1200)
            actualPoints = awaitItem()
            assertThat(actualPoints == 0).isTrue()
        }
    }
}