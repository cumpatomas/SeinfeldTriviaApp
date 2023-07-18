package com.cumpatomas.seinfeldrecords.ui.quizFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.cumpatomas.seinfeldrecords.core.ex.hideKeyboard
import com.cumpatomas.seinfeldrecords.core.ex.typeWrite
import com.cumpatomas.seinfeldrecords.data.RandomResponseText
import com.cumpatomas.seinfeldrecords.databinding.QuizFragmentBinding
import com.cumpatomas.seinfeldrecords.ui.RoundedDialog
import com.robinhood.ticker.TickerUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuizFragment : Fragment() {
    private val viewModel: QuizFragmentViewModel by viewModels()
    private var _binding: QuizFragmentBinding? = null
    private val binding get() = _binding!!
    private var correctAnswer = ""
    private var submittedAnswer = ""
    private var answerToList = listOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = QuizFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel
        initCollectors()
        initListeners()
        if (viewModel.questionAnswered) {
            setScreen()
        }
    }

    private fun setScreen() {
        if (viewModel.answerIsCorrect)
            setCorrectAnswerScreen()
        else setWrongAnswerScreen()
    }

    private fun initListeners() {
        binding.tiAnswer.hint =
            if (answerToList.lastIndex == 0) "${answerToList.lastIndex + 1} word..." else "${answerToList.lastIndex + 1} words..."

        binding.tiAnswer.addTextChangedListener { input ->
            submittedAnswer = input.toString()
        }

        binding.btSubmitButton.setOnClickListener {
            viewModel.questionAnswered = true
            checkAnswer(submittedAnswer)
            binding.tiAnswer.setText("")
        }
        binding.btNext.setOnClickListener {
            viewModel.questionAnswered = false
            viewModel.getNewQuestion()
            binding.tiAnswer.setText("")
            binding.animationConfetti.isGone = true
            binding.animationWrong.isGone = true
            binding.tiAnswer.isVisible = true
            binding.btSubmitButton.isVisible = true
            binding.btNext.isGone = true
            binding.tvResponse.isGone = true
        }
    }

    private fun checkAnswer(answer: String) {
        if (answer.lowercase().trimEnd() == correctAnswer.lowercase()) {
            viewModel.randomResponseText = RandomResponseText().getAnswer(true)
            setCorrectAnswerScreen()
            viewModel.setPoints(1)
            if (viewModel.questionsCorrect.value % 5 == 0) {
                RoundedDialog(
                    "Having a good look Costanza??\nDon't be a bad tipper...buy me a coffee!",
                    "Buy",
                    "https://paypal.me/cumpatomas"
                ).show(parentFragmentManager, "Coffee")
            }
        } else {
            viewModel.randomResponseText = RandomResponseText().getAnswer(false)
            viewModel.setPoints(-1)
            setWrongAnswerScreen()
        }
    }

    private fun setWrongAnswerScreen() {
        hideKeyboard()
        viewModel.answerIsCorrect = false
        binding.animationConfetti.isGone = true
        binding.animationWrong.isVisible = true
        binding.tiAnswer.isGone = true
        binding.btSubmitButton.isGone = true
        binding.btNext.isVisible = true
        binding.tvResponse.typeWrite(viewLifecycleOwner, viewModel.randomResponseText, 20L)
        binding.tvResponse.isVisible = true
        binding.tvResponse.text = viewModel.randomResponseText
    }

    private fun setCorrectAnswerScreen() {
        hideKeyboard()
        viewModel.answerIsCorrect = true
        binding.animationConfetti.isVisible = true
        binding.animationWrong.isGone = true
        binding.tiAnswer.isGone = true
        binding.btSubmitButton.isGone = true
        binding.btNext.isVisible = true
        binding.tvResponse.typeWrite(viewLifecycleOwner, viewModel.randomResponseText, 20L)
        binding.tvResponse.isVisible = true
        binding.tvResponse.text = viewModel.randomResponseText
    }

    @SuppressLint("SetTextI18n")
    private fun initCollectors() {
        lifecycleScope.launch {
            viewModel.getPoints()

            launch() {
                viewModel.userPoints.collectLatest {
                    val ticker = binding.quizPointsTickerView
                    ticker.setCharacterLists(TickerUtils.provideNumberList())
                    ticker.text = it.toString()
                }
            }
            launch {
                viewModel.randomQuestion.collectLatest {
                    if (it.isNotEmpty()) {
                        if (!viewModel.questionAnswered) {
                            binding.tvQuestion.typeWrite(viewLifecycleOwner, it, 10L)
                        } else {
                            binding.tvQuestion.text = it
                        }
                    }
                }
            }
            launch {
                viewModel.correctAnswer.collectLatest {
                    correctAnswer = it
                    answerToList = correctAnswer.split(" ")
                    binding.tiAnswer.hint =
                        if (answerToList.lastIndex == 0) "${answerToList.lastIndex + 1} word..." else "${answerToList.lastIndex + 1} words..."
                }
            }

            launch {
                viewModel.questionsCorrect.collectLatest { correctAnswer ->
                    viewModel.totalQuestions.collectLatest { total ->
                        binding.tvQuestionsScore.text =
                            "$correctAnswer out of $total"
                    }
                }
            }
        }
    }
}