package com.cumpatomas.seinfeldrecords.ui.homefragment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import app.cash.turbine.timeout
import com.cumpatomas.seinfeldrecords.domain.GetRandomScript
import com.cumpatomas.seinfeldrecords.domain.GetUserPoints
import com.cumpatomas.seinfeldrecords.domain.SaveUserPoints
import com.cumpatomas.seinfeldrecords.domain.ScrapScripts
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
    private val scraping: ScrapScripts = mockk()
    private val randomScript: GetRandomScript = mockk()
    lateinit var viewModel: HomeFragmentViewModel
    private val getPoints: GetUserPoints = mockk()
    private val updatePoints: SaveUserPoints = mockk()
    private val listTest = mutableListOf("uno", "dos", "tres")

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
            val new = awaitItem()
            assertThat(new != 15).isTrue()
            viewModel.resetCounter()
            assertThat(actualTime == 15).isTrue()
        }
    }
}