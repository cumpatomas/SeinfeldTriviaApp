package com.cumpatomas.seinfeldrecords.ui.chargestures

import android.annotation.SuppressLint
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.cumpatomas.seinfeldrecords.R
import com.cumpatomas.seinfeldrecords.data.model.CharGestures
import com.cumpatomas.seinfeldrecords.databinding.CharGesturesFragmentBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException

class CharGesturesFragment : Fragment() {
    private val viewModel: CharGesturesViewModel by viewModels()
    private val adapter = CharGesturesAdapter()
    private var _binding: CharGesturesFragmentBinding? = null
    private val binding get() = _binding!!
    private var gesturesList = emptyList<CharGestures>()
    lateinit var randomGesture: CharGestures

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CharGesturesFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        /** Marquee configuration*/
        val txtMarquee: TextView = view.findViewById(R.id.tvExplanation)
        txtMarquee.isSelected = true
        txtMarquee.isSingleLine = true
        initListeners()
        initRecyclerView()
        initCollectors()
    }

    private fun initCollectors() {
        lifecycleScope.launch {
            viewModel.gesturesList.collectLatest { recievedList ->
                updateList(recievedList)
                gesturesList = recievedList
                randomGesture = recievedList.shuffled().random()
            }
        }
    }

    private fun initListeners() {

        setButtonRandomAudio()
        lifecycleScope.launch {

            viewModel.buttonIsPlaying.collectLatest { buttonIsPlaying ->
                binding.btPlayThePhrase.isEnabled = !buttonIsPlaying
                binding.lottieSound.isVisible = buttonIsPlaying
                if (!buttonIsPlaying) {
                    binding.btPlayThePhrase.isVisible = true
                } else {
                    binding.btPlayThePhrase.isVisible = false
                }
            }
        }

    }

    private fun setButtonRandomAudio() {
        binding.btPlayThePhrase.setOnClickListener {
            playAudio(randomGesture.audioLink)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun playAudio(url: String) {
        val mediaPlayer = MediaPlayer()
//        mediaPlayer.setAudioStreamType((AudioManager.STREAM_MUSIC))
        mediaPlayer.setAudioAttributes(
            AudioAttributes
                .Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        );
        try {
            if (mediaPlayer.isPlaying) {
                viewModel.buttonPlay(true)
            }
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        //mp3 will be started after completion of preparing...
        mediaPlayer.setOnPreparedListener(MediaPlayer.OnPreparedListener { player ->
            player.start()
            viewModel.buttonPlay(true)
        })

        mediaPlayer.setOnCompletionListener {
            it.pause()
            it.stop()
            viewModel.buttonPlay(false)
        }
    }

    private fun initRecyclerView() {
        val recyclerView =
            binding.rvCharGesturesRecycler// encontramos el Recycler del Main LAYOUT xml
        recyclerView.layoutManager =
            GridLayoutManager(
                context,
                3
            ) // si cambiamos el Manager aqui podriamos hacer listados de GRID u otro tipo! Investigar!
        recyclerView.adapter = this.adapter
    }

    private fun updateList(list: List<CharGestures>) {
        adapter.setList(list = list)
    }
}