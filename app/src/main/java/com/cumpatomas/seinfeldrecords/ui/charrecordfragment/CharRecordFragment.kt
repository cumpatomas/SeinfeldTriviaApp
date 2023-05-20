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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.cumpatomas.seinfeldrecords.R
import com.cumpatomas.seinfeldrecords.data.model.CharRecord
import com.cumpatomas.seinfeldrecords.databinding.CharRecordFragmentBinding
import com.cumpatomas.seinfeldrecords.ui.charrecordlist.CharRecordListFragmentDirections
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class CharRecordFragment : Fragment() {

    private var _binding: CharRecordFragmentBinding? = null
    private val binding get() = _binding!!

    private val navArgs: CharRecordFragmentArgs by navArgs()
    private val viewModel: CharRecordViewModel by viewModels()

    var recordVideoId: String? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            CharRecordFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        receiveArguments()
        initCollectors()





    }

    private fun receiveArguments() {
        viewModel.receiveArguments(navArgs.recordid)
    }

    private fun initCollectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val job = launch {
                    viewModel.record.collectLatest { record ->

                        recordVideoId = record?.videoid

                        updateRecordWithInfo(record)


                    }


                }
                job.join()

            }
        }


    }

    private fun updateRecordWithInfo(record: CharRecord?) {

        binding.tvTitle.text = record?.name
        binding.tvRecordDescription.text = record?.description
        setVideoFragment(record?.videoid)

/*        val binding = VideoPlayerFragmentBinding()


            val youTubePlayerView: YouTubePlayerView = binding.youtubePlayerView
            lifecycle.addObserver(youTubePlayerView)

            val video = viewModel.setVideo(record)

            binding.youtubePlayerView.addYouTubePlayerListener(video)
            youTubePlayerView.enterFullScreen()*/

    }


    private fun initListeners() {



    }


    private fun setVideoFragment(videoId: String?) {

        val videoFragment = VideoPlayerFragment()

        childFragmentManager.beginTransaction().apply {
            add(R.id.youtubePlayerFragment, videoFragment)

            commit()



        }
    }
}



