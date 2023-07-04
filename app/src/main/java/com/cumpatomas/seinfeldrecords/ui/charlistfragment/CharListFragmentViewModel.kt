package com.cumpatomas.seinfeldrecords.ui.charlistfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cumpatomas.seinfeldrecords.data.CharListProvider
import com.cumpatomas.seinfeldrecords.data.model.CharRecord
import com.cumpatomas.seinfeldrecords.data.model.SeinfeldChar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class CharListFragmentViewModel : ViewModel() {

    private val _charList = MutableStateFlow<List<SeinfeldChar>>(emptyList())
    val charList = _charList.asStateFlow()

    private val _charRecords = MutableStateFlow<List<CharRecord>>(emptyList())
    val charRecords = _charRecords.asStateFlow()

    // Declaramos el Estado de la Vista para actualizar
    private val _viewState = Channel<CharListViewState>()
    val viewState = _viewState.receiveAsFlow()

    init {

        viewModelScope.launch(Dispatchers.IO) {
            _viewState.send(CharListViewState(loading = true))


            _charList.value = CharListProvider.charList

            _viewState.send(CharListViewState(loading = false))

        }
    }
}