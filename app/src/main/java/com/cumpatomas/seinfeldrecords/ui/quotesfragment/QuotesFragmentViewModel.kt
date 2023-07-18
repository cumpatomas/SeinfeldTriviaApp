package com.cumpatomas.seinfeldrecords.ui.quotesfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cumpatomas.seinfeldrecords.domain.GetQuotesUsecase
import com.cumpatomas.seinfeldrecords.domain.GetUserPoints
import com.cumpatomas.seinfeldrecords.domain.MAX_POINTS
import com.cumpatomas.seinfeldrecords.domain.SaveUserPoints
import com.cumpatomas.seinfeldrecords.domain.ScrapScripts
import com.cumpatomas.seinfeldrecords.domain.ZERO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuotesFragmentViewModel @Inject constructor(
    private val getQuotes: GetQuotesUsecase,
    private val getPoints: GetUserPoints,
    private val updatePoints: SaveUserPoints,
    private val getScripts: ScrapScripts
) : ViewModel() {
    private val _quotesListViewModel = MutableStateFlow(emptyList<QuoteItem>())
    val quotesListViewModel = _quotesListViewModel.asStateFlow()
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()
    private val _userPoints = MutableStateFlow<Int>(0)
    val userPoints = _userPoints.asStateFlow()
    private val _link = MutableStateFlow("")
    val link = _link.asStateFlow()
    private val _reloadTimes= MutableStateFlow<Int>(0)
    val reloadTimes = _reloadTimes.asStateFlow()

    init {
        _isLoading.value = true
        viewModelScope.launch(IO) {
            getUserPoints()
            while (_quotesListViewModel.value.isEmpty()) {
                launch {
                    _link.value = getScripts.invoke().shuffled().random()
                }.join()

                launch {
                    _quotesListViewModel.value = getQuotes.invoke(_link.value)
                }.join()
            }
            _isLoading.value = false
        }
    }

    fun updateList() {
        _isLoading.value = true
        _quotesListViewModel.value = emptyList()
        viewModelScope.launch(IO) {
            while (_quotesListViewModel.value.isEmpty()) {
                launch {
                    _link.value = getScripts.invoke().shuffled().random()
                }.join()

                launch {
                    _quotesListViewModel.value = getQuotes.invoke(_link.value)
                }.join()
            }
            _isLoading.value = false
        }
    }

    suspend fun getUserPoints() {
        viewModelScope.launch(IO) {
            _userPoints.value = getPoints.invoke()
            delay(2000)
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
                    updatePoints.invoke(_userPoints.value)
                } else {
                    _userPoints.value = ZERO
                    updatePoints.invoke(_userPoints.value)
                }
            }
        }
    }
    fun reLoadCounting() {
        _reloadTimes.value ++
    }

    fun resetReloadTimes() {
        _reloadTimes.value = 0
    }
}