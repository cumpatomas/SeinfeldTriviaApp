package com.cumpatomas.seinfeldrecords.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cumpatomas.seinfeldrecords.domain.SetNoAdsForUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel@Inject constructor(
    private val setNoAdsForUser: SetNoAdsForUser
): ViewModel() {
    fun setNoAdsForUser() {
        viewModelScope.launch {
            setNoAdsForUser.invoke()
        }
    }
}