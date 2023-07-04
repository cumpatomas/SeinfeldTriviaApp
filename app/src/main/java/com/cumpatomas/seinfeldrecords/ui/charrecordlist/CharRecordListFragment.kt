package com.cumpatomas.seinfeldrecords.ui.charrecordlist

import android.annotation.SuppressLint
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.cumpatomas.seinfeldrecords.R
import com.cumpatomas.seinfeldrecords.adapter.RecordListAdapter
import com.cumpatomas.seinfeldrecords.databinding.RecordListFragmentBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException

class CharRecordListFragment : Fragment() {
    private val viewmodel: RecordListViewModel by viewModels()
    private val adapter = RecordListAdapter()
    private var _binding: RecordListFragmentBinding? = null
    private val binding get() = _binding!!
    private val navArgs: CharRecordListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RecordListFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        receiveArguments()
        initListeners()
        initRecyclerView()
        initCollectors()
    }

    private fun initCollectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewmodel.recordList.collectLatest { list ->
                        adapter.setList(list)
                        binding.tvTotalNumberRecords.text = list.size.toString()
                    }
                }
            }
        }
    }

    private fun initRecyclerView() {
        val recyclerView = binding.rvRecordList// encontramos el Recycler del Main LAYOUT xml
        recyclerView.layoutManager =
            LinearLayoutManager(context) // si cambiamos el Manager aqui podriamos hacer listados de GRID u otro tipo! Investigar!
        recyclerView.adapter = this.adapter
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initListeners() {
        binding.btPlayAudio.isEnabled = true
        binding.btPlayAudio.setOnClickListener {
            playAudio()
        }
        val totalRecords = getString(R.string.char_total_records)
        val charSelected = navArgs.selectedCharRecord.substring(
            0,
            navArgs.selectedCharRecord.indexOf(' ')
        ); // formula to get the first name only
        binding.tvNameAndRecords.text = "$charSelected's $totalRecords"

        adapter.onItemClickListener = { id, _ ->
            /*val action =
                CharRecordListFragmentDirections.(id)
            findNavController().navigate(action)*/
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun playAudio() {
        binding.btPlayAudio.isEnabled = false
        var url = "https://recetas-de-bruno.firebaseapp.com/loop1.wav"
        val mediaPlayer = MediaPlayer()
//        mediaPlayer.setAudioStreamType((AudioManager.STREAM_MUSIC))
        mediaPlayer.setAudioAttributes(
            AudioAttributes
                .Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        );
        try {
            mediaPlayer.setDataSource(url)
//            lifecycleScope.launch(IO) {
//                mediaPlayer.prepareAsync() // prepare async to not block main thread
//            }
            mediaPlayer.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        //mp3 will be started after completion of preparing...
        mediaPlayer.setOnPreparedListener(OnPreparedListener { player ->
            player.start()
        })

        mediaPlayer.setOnCompletionListener {
            binding.btPlayAudio.isEnabled = true
            it.stop()
        }
        /*        val player = ExoPlayer.Builder(this.requireContext())
                    .setMediaSourceFactory(DefaultMediaSourceFactory(this.requireContext()).setLiveTargetOffsetMs(1000))
                    .build()
                val mediaItem =
                MediaItem.Builder()
                    .setUri("https://recetas-de-bruno.firebaseapp.com/loop1.wav")
                    .setLiveConfiguration(
                        MediaItem.LiveConfiguration.Builder().setMaxPlaybackSpeed(1.02f).build()
                    )
                    .build()
                player.setMediaItem(mediaItem)
                player.prepare()
                player.play()

                player.addListener(
                    object : Player.Listener {
                        override fun onIsPlayingChanged(isPlaying: Boolean) {
                            if (isPlaying) {
                                binding.btPlayAudio.isEnabled = false
                            } else {
                                binding.btPlayAudio.isEnabled = true
        player.release()
                            }
                        }
                    }
                )
                player.setMediaItem(mediaItem)
                player.prepare()
                player.play()
                binding.btPlayAudio.isEnabled = false*/
    }

    private fun receiveArguments() {
        viewmodel.receiveArguments(navArgs.selectedCharRecord)
    }
}