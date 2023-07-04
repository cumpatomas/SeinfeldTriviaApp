package com.cumpatomas.seinfeldrecords.ui.charrecordlist

import androidx.lifecycle.ViewModel
import com.cumpatomas.seinfeldrecords.data.model.CharRecord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RecordListViewModel : ViewModel() {

    private val _recordList = MutableStateFlow<List<CharRecord>>(emptyList())
    val recordList = _recordList.asStateFlow()

    var selectedChar: String? = null
        private set



    fun receiveArguments(selectedChar: String) {
        this.selectedChar = selectedChar
      /*  viewModelScope.launch(Dispatchers.IO) {

            val recordListJob = launch {
                _recordList.value = GetRecordListUseCase().invoke().filter {
                    it.mainChar == selectedChar
                }

            }
           recordListJob.join()

        }*/
    }

}


