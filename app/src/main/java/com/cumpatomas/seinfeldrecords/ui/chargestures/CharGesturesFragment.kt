package com.cumpatomas.seinfeldrecords.ui.chargestures

import android.annotation.SuppressLint
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.cumpatomas.seinfeldrecords.R
import com.cumpatomas.seinfeldrecords.data.database.RandomGifProvider
import com.cumpatomas.seinfeldrecords.data.model.CharGestures
import com.cumpatomas.seinfeldrecords.databinding.CharGesturesFragmentBinding
import com.robinhood.ticker.TickerUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException

const val CORRECT_AUDIO = "https://seinfeldapp-29d5f.web.app/audios/correct_answer_plop.mp3"
const val WRONG_AUDIO = "https://seinfeldapp-29d5f.web.app/audios/wrong_answer.mp3"
const val TEN_POINTS_AUDIO = "https://seinfeldapp-29d5f.web.app/audios/win_10_points.mp3"
const val WIN_TEXT = "You win 10 points Mr. Eyebrow!    You win 10 points Mr Eyebrow!"

@AndroidEntryPoint
class CharGesturesFragment : Fragment() {
    private val viewModel: CharGesturesViewModel by viewModels()
    private val adapter = CharGesturesAdapter()
    private var _binding: CharGesturesFragmentBinding? = null
    private val binding get() = _binding!!
    private var gesturesList = mutableListOf<CharGestures>()
    private val navArgs: CharGesturesFragmentArgs by navArgs()

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
        viewModel.getCharSelected(navArgs.selectedChar)
        initRecyclerView()
        initCollectors()
        initListeners()
    }

    private fun initCollectors() {
        lifecycleScope.launch {
            launch() {
                viewModel.userPoints.collectLatest {
                    val ticker = binding.pointsTickerViewCharList
                    ticker.setCharacterLists(TickerUtils.provideNumberList())
                    ticker.text = it.toString()
                }
            }
            launch {
                viewModel.gesturesList.collectLatest { recievedList ->
                    updateList(recievedList.toMutableList())
                    gesturesList = recievedList.toMutableList()
                }
            }.join()
        }
    }

    private fun initListeners() {
        lifecycleScope.launch {
            launch {
                viewModel.loading.collectLatest { loading ->
                    binding.progressBar.isVisible = loading
                    if (!loading) {
                        setButtonRandomAudio()
                        binding.btPlayThePhrase.alpha = 1f
                    } else
                        binding.btPlayThePhrase.alpha = 0.0f
                }
            }

            launch {
                viewModel.buttonIsPlaying.collectLatest { buttonIsPlaying ->
                    binding.btPlayThePhrase.isClickable = !buttonIsPlaying
                    binding.lottieSound.isVisible = buttonIsPlaying
                    binding.btPlayThePhrase.isVisible = !buttonIsPlaying
                }
            }

            launch {
                viewModel.gesturesList.collectLatest { counting ->
                    viewModel.questionsCorrect.collectLatest { correctAnswer ->
                        if (correctAnswer > counting.size) {
                            binding.tvQuestionsScore.text =
                                "${counting.size} out of ${counting.size}"
                        } else {
                            binding.tvQuestionsScore.text =
                                "$correctAnswer out of ${counting.size}"
                        }
                    }
                }
            }
        }
    }

    private fun setButtonRandomAudio() {
        binding.btPlayThePhrase.setOnClickListener {
            val randomAudio = gesturesList.filter { it.id == viewModel.randomGestureId.value }

            playAudio(randomAudio[0].audioLink)
        }

        adapter.onItemClickListener = {
            if (viewModel.randomGestureId.value == gesturesList[gesturesList.indexOf(it)].id) {
                gesturesList[gesturesList.indexOf(it)].clicked = true

                playAudio(CORRECT_AUDIO)
                updateList(gesturesList)
                viewModel.getRandomId()
                viewModel.countQuestion()
                checkIfWin()
            } else {
                gesturesList.forEach { it.clicked = false }

                playAudio(WRONG_AUDIO)
                updateList(gesturesList)
                viewModel.getRandomId()
                viewModel.setPoints(-1)
                viewModel.resetQuestion()
            }
        }
    }

    private fun checkIfWin() {
        for (gesture in gesturesList) {
            if (gesture.clicked) continue
            else return
        }
        winAnimation()
    }

    private fun winAnimation() {
        val randomWrongGif = RandomGifProvider().randomCorrectGif
        playAudio(TEN_POINTS_AUDIO)
        viewModel.setPoints(10)
        viewModel.setCharScreenComplete(navArgs.selectedChar)
        binding.rvCharGesturesRecycler.isGone = true
        binding.lottieSound.isGone = true
        binding.animationConfettiGestures.isVisible = true
        binding.tvWinTenPoints.isVisible = true
        binding.lottieWin.isVisible = true
        binding.tvExplanation.text = WIN_TEXT
        binding.gifGestureContainer.isVisible = true
        binding.gifGestureContainer.setImageResource(randomWrongGif.shuffled().random())
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun playAudio(url: String) {
        val mediaPlayer = MediaPlayer()
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
//        mediaPlayer.setAudioStreamType((AudioManager.STREAM_MUSIC))
        mediaPlayer.setAudioAttributes(
            AudioAttributes
                .Builder()
                .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        )

        try {
            viewModel.buttonPlay(true)
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepare()
            //mp3 will be started after completion of preparing...
            mediaPlayer.setOnPreparedListener(MediaPlayer.OnPreparedListener { player ->
                player.start()

                if (url == CORRECT_AUDIO || url == WRONG_AUDIO) {
                    viewModel.buttonPlay(false)
                } else {
                    viewModel.buttonPlay(true)
                }
                if (url == TEN_POINTS_AUDIO) {
                    viewModel.buttonPlay(false)
                    binding.btPlayThePhrase.isGone = true
                }
                mediaPlayer.setOnCompletionListener {
                    viewModel.buttonPlay(false)
                    binding.btPlayThePhrase.isVisible = true
                }
            })
        } catch (e: IOException) {
            e.printStackTrace()
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

    private fun updateList(list: MutableList<CharGestures>) {
        adapter.setList(list = list)
    }
}