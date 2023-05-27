package com.cumpatomas.seinfeldrecords.ui.charrecordfragment

import com.cumpatomas.seinfeldrecords.data.model.CharRecord
import com.cumpatomas.seinfeldrecords.domain.GetRecordbyId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.cumpatomas.seinfeldrecords.ui.charrecordlist.CharRecordListFragmentDirections
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.Dispatchers

class CharRecordViewModel : ViewModel() {

    private val getRecordById = GetRecordbyId()

    var id: Int? = null
        private set

    private val _record = MutableStateFlow<CharRecord?>(null)
    val record = _record.asStateFlow()


    fun receiveArguments(id: Int) {
        this.id = id
/*        viewModelScope.launch(Dispatchers.IO) {
            val job = launch {

                _record.value = getRecordById.invoke(this@CharRecordViewModel.id)
            }
            job.join()


        }*/

    }

    fun setVideo(record: CharRecord?): AbstractYouTubePlayerListener {
        return object :
            AbstractYouTubePlayerListener() {

            override fun onReady(youTubePlayer: YouTubePlayer) {
                val videoId = record?.videoid
                youTubePlayer.loadVideo(videoId.toString(), 0f)


            }
        }


    }

}