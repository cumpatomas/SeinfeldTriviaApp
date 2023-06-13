package com.cumpatomas.seinfeldrecords.ui.quotesfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cumpatomas.seinfeldrecords.domain.GetQuotesUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuotesFragmentViewModel @Inject constructor(
    private val getQuotes: GetQuotesUsecase
) : ViewModel() {
    private val _quotesListViewModel = MutableStateFlow(emptyList<QuoteItem>())
    val quotesListViewModel = _quotesListViewModel.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch(IO) {
            _isLoading.value = true
            launch {
                _quotesListViewModel.value = getQuotes.invoke()
            }.join()
            _isLoading.value = false

        }
    }
    fun updateList() {
        _isLoading.value = true
        viewModelScope.launch(IO) {

            launch {
                _quotesListViewModel.value = getQuotes.invoke()
            }.join()
            _isLoading.value = false
        }

    }
}