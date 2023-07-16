package com.cumpatomas.seinfeldrecords.ui.charlistfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cumpatomas.seinfeldrecords.data.database.GestureDao
import com.cumpatomas.seinfeldrecords.data.database.entities.toModel
import com.cumpatomas.seinfeldrecords.data.model.CharGestures
import com.cumpatomas.seinfeldrecords.data.model.CharRecord
import com.cumpatomas.seinfeldrecords.data.model.SeinfeldChar
import com.cumpatomas.seinfeldrecords.domain.GetCharListUseCase
import com.cumpatomas.seinfeldrecords.domain.GetUserPoints
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharListFragmentViewModel @Inject constructor(
    private val charProvider: GetCharListUseCase,
    private val gesturesProvider: GestureDao,
    private val pointsProvider: GetUserPoints,
) :
    ViewModel() {
    private val _charList = MutableStateFlow<List<SeinfeldChar>>(emptyList())
    val charList = _charList.asStateFlow()
    private val _gesturesList = MutableStateFlow<List<CharGestures>>(emptyList())
    val gesturesList = _gesturesList.asStateFlow()
    private val _animationIn = MutableStateFlow(false)
    val animationIn = _animationIn.asStateFlow()
    private val _pointsCircle = MutableStateFlow(false)
    val pointsCircle = _pointsCircle.asStateFlow()
    private val _userPoints = MutableStateFlow<Int>(0)
    val userPoints = _userPoints.asStateFlow()

    // Declaramos el Estado de la Vista para actualizar
    private val _viewState = Channel<CharListViewState>()
    val viewState = _viewState.receiveAsFlow()

    init {
        viewModelScope.launch(IO) {
            launch {
                _userPoints.value = pointsProvider.invoke()
                delay(2000)
            }

            launch {
                _gesturesList.value = gesturesProvider.getGestureList().map { it.toModel() }
            }.join()

            _viewState.send(CharListViewState(loading = true))

            _charList.value = charProvider.invoke()

            _viewState.send(CharListViewState(loading = false))
            delay(600)
            _animationIn.value = true
            delay(6000)
            _animationIn.value = false
            delay(1000)
            _pointsCircle.value = true

        }


    }

    suspend fun getUserPoints() {
        viewModelScope.launch(IO) {
            _userPoints.value = pointsProvider.invoke()
            delay(2000)
        }
    }

    fun getGestures() {
        viewModelScope.launch(IO) {
            launch {
                _gesturesList.value = gesturesProvider.getGestureList().map { it.toModel() }
            }.join()
        }
    }
}