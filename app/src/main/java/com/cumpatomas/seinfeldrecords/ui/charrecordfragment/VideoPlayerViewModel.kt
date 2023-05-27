package com.cumpatomas.seinfeldrecords.ui.charrecordfragment


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cumpatomas.seinfeldrecords.data.model.CharRecord
import com.cumpatomas.seinfeldrecords.domain.GetRecordbyId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VideoPlayerViewModel: ViewModel() {

    var id: Int? = null
    private val getRecordById = GetRecordbyId()
    private val _selectedChar = MutableStateFlow<CharRecord?>(null)
    val selectedChar = _selectedChar.asStateFlow()

    fun receiveArguments(recordid: Int?) {
       /* this.id = recordid
        viewModelScope.launch(Dispatchers.IO) {
            val job = launch {
                _selectedChar.value = getRecordById.invoke(this@VideoPlayerViewModel.id)
            }
            job.join()
        }
*/

    }


}