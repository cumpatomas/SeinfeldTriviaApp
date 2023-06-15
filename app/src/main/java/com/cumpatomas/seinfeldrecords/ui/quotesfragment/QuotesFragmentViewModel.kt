package com.cumpatomas.seinfeldrecords.ui.quotesfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cumpatomas.seinfeldrecords.domain.GetQuotesUsecase
import com.cumpatomas.seinfeldrecords.domain.GetUserPoints
import com.cumpatomas.seinfeldrecords.domain.SaveUserPoints
import com.cumpatomas.seinfeldrecords.domain.ScrapScripts
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

    init {
        _isLoading.value = true
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
            _userPoints.value += points
            if (_userPoints.value < 0) {
                _userPoints.value = 0
            }
            launch {
                updatePoints.invoke(_userPoints.value)
            }.join()
        }
    }
}