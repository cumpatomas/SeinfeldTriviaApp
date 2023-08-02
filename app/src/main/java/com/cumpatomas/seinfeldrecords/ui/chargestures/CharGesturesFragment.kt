package com.cumpatomas.seinfeldrecords.ui.chargestures

import android.annotation.SuppressLint
import android.media.AudioAttributes
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
import com.cumpatomas.seinfeldrecords.ui.RoundedDialog
import com.robinhood.ticker.TickerUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
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

    @SuppressLint("SetTextI18n")
    private fun initListeners() {
        lifecycleScope.launch {
            launch {
                viewModel.loading.collectLatest { loading ->
                    binding.progressBar.isVisible = loading
                    if (!loading) {
                        setButtonRandomAudio()
                        binding.btPlayThePhrase.isVisible = true
                    } else
                        binding.btPlayThePhrase.isGone = true
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

        adapter.onItemClickListener = { charGesture ->
            if (viewModel.randomGestureId.value == gesturesList[gesturesList.indexOf(charGesture)].id) {
                gesturesList[gesturesList.indexOf(charGesture)].clicked = true

                playAudio(CORRECT_AUDIO, true)
                updateList(gesturesList)
                viewModel.getRandomId()
                viewModel.countQuestion()
                checkIfWin()
            } else {
                gesturesList.forEach { it.clicked = false }

                playAudio(WRONG_AUDIO, true)
                updateList(gesturesList)
                viewModel.getRandomId()
                viewModel.setPoints(-1)
                viewModel.resetQuestion()
            }
        }
    }

    private fun setButtonRandomAudio() {
        binding.btPlayThePhrase.setOnClickListener {
            val randomAudio = gesturesList.filter { it.id == viewModel.randomGestureId.value }

            playAudio(randomAudio[0].audioLink)
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
        playAudio(TEN_POINTS_AUDIO, true)
        viewModel.setPoints(10)
        binding.btPlayThePhrase.isGone = true
        viewModel.setCharScreenComplete(navArgs.selectedChar)
        binding.rvCharGesturesRecycler.isGone = true
        binding.lottieSound.isGone = true
        binding.animationConfettiGestures.isVisible = true
        binding.tvWinTenPoints.isVisible = true
        binding.lottieWin.isVisible = true
        binding.tvExplanation.text = WIN_TEXT
        binding.gifGestureContainer.isVisible = true
        binding.gifGestureContainer.setImageResource(randomWrongGif.shuffled().random())
        lifecycleScope.launch {
            delay(4000)
            RoundedDialog(
                "Having a good look Costanza??\nDon't be a bad tipper...buy me a coffee!",
                "Buy",
                "https://paypal.me/cumpatomas"
            ).show(parentFragmentManager, "Coffee")
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun playAudio(url: String, rightOrWrong: Boolean = false) {
        val mediaPlayer: MediaPlayer = MediaPlayer()
        /*        if (mediaPlayer?.isPlaying == true) {
                    mediaPlayer.stop()
                    mediaPlayer.release()
                    mediaPlayer = null
                }*/
        mediaPlayer.setAudioAttributes(
            AudioAttributes
                .Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )

        try {
            if (!rightOrWrong) {
                binding.lottieSound.isVisible = true
                binding.btPlayThePhrase.isClickable = false
                binding.btPlayThePhrase.isGone = true
            }

            mediaPlayer.setDataSource(url)
            mediaPlayer.prepare()
            //mp3 will be started after completion of preparing...
            mediaPlayer.setOnPreparedListener { player ->
                player.start()
                mediaPlayer.setOnCompletionListener {
                    if (!rightOrWrong) {
                        binding.btPlayThePhrase.isClickable = true
                        binding.btPlayThePhrase.isVisible = true
                        binding.lottieSound.isGone = true
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            mediaPlayer.stop()
            mediaPlayer.release()
            binding.btPlayThePhrase.isClickable = true
            binding.btPlayThePhrase.isVisible = true
            binding.lottieSound.isGone = true
            playAudio(url)
        }
    }

    private fun initRecyclerView() {
        val recyclerView =
            binding.rvCharGesturesRecycler
        recyclerView.layoutManager =
            GridLayoutManager(
                context,
                3
            )
        recyclerView.adapter = this.adapter
    }

    private fun updateList(list: MutableList<CharGestures>) {
        adapter.setList(list = list)
    }
}