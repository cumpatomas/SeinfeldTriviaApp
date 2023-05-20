package com.cumpatomas.seinfeldrecords.ui.charrecordfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.cumpatomas.seinfeldrecords.databinding.VideoPlayerFragmentBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class VideoPlayerFragment : Fragment() {


    private var _binding: VideoPlayerFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VideoPlayerViewModel by viewModels()
    private val navArgs: CharRecordFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            VideoPlayerFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        receiveArguments()
        initCollectors()


    }

    private fun receiveArguments() {
       viewModel.receiveArguments(navArgs.recordid)
//        loadVideo(selectedRecord)
    }

    private fun initCollectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {

                    viewModel.selectedChar.collectLatest { record ->
                        loadVideo(record?.videoid)


                    }

                }

            }
        }


    }

    private fun loadVideo(recordid: String?) {

        val youTubePlayerView: YouTubePlayerView = binding.youtubePlayerView
        lifecycle.addObserver(youTubePlayerView)

        binding.youtubePlayerView.addYouTubePlayerListener(object :
            AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(recordid.toString(), 0f)

            }
        })
    }
}