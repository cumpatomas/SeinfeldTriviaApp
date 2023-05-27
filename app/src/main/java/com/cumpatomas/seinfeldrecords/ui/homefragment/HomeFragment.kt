package com.cumpatomas.seinfeldrecords

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.cumpatomas.seinfeldrecords.data.database.RandomGifProvider
import com.cumpatomas.seinfeldrecords.databinding.HomeFragmentBinding
import com.cumpatomas.seinfeldrecords.ui.homefragment.HomeFragmentViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private val viewModel: HomeFragmentViewModel by viewModels()
    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!
    var correctTitle = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = HomeFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initCollectors()
        initListeners()
        if (viewModel.gifActive) {
            setScreen()
        }
    }

    private fun setScreen() {
        if (viewModel.correctAnswer)
            setCorrectAnswerScreen()
        else
            setWrongAnswerScreen()
    }

    private fun initListeners() {
        lifecycleScope.launch() {
            binding.btNext.isEnabled = false
            viewModel.timer.collectLatest {
                binding.tvCounter.text = it.toString()
                if (it <= 5) binding.tvCounter.setTextColor(resources.getColor(R.color.red))
                if (it == 0) {
                    binding.answersContainer.visibility = INVISIBLE
                    binding.btNext.isEnabled = true
                }
            }
        }

        binding.btNext.setOnClickListener {
            viewModel.gifActive = false
            binding.tvCounter.setTextColor(resources.getColor(R.color.primaryColor))
            viewModel.resetCounter()
            binding.btNext.isEnabled = false
            binding.tvScript.text = ""
            binding.tvCounter.setTextColor(resources.getColor(R.color.primaryColor))
            binding.gifContainer.isGone = true
            binding.tvSolution.text = ""
            lifecycleScope.launch {

                launch {
                    binding.answersContainer.isGone = true
                    viewModel.getNewScript()
                    binding.progressBar.isVisible = viewModel.loading.value

                }.join()
                getScriptText()
                binding.tvScript.isVisible = true
                binding.tvCounter.isVisible = true
            }
        }
        binding.cvAnswer1.setOnClickListener {
            viewModel.gifActive = true
            if (binding.tvAnswer1.text == correctTitle) {
                viewModel.setPoints(1)
                setCorrectAnswerScreen()
            } else {
                viewModel.setPoints(-1)
                setWrongAnswerScreen()
            }
        }
        binding.cvAnswer2.setOnClickListener {
            viewModel.gifActive = true
            if (binding.tvAnswer2.text == correctTitle) {
                viewModel.setPoints(1)
                setCorrectAnswerScreen()
            } else {
                viewModel.setPoints(-1)
                setWrongAnswerScreen()
            }
        }
        binding.cvAnswer3.setOnClickListener {
            viewModel.gifActive = true
            if (binding.tvAnswer3.text == correctTitle) {
                viewModel.setPoints(1)
                setCorrectAnswerScreen()
            } else {
                viewModel.setPoints(-1)
                setWrongAnswerScreen()
            }
        }
        binding.cvAnswer4.setOnClickListener {
            viewModel.gifActive = true
            if (binding.tvAnswer4.text == correctTitle) {
                viewModel.setPoints(1)
                setCorrectAnswerScreen()
            } else {
                viewModel.setPoints(-1)
                setWrongAnswerScreen()
            }
        }
    }

    private fun setWrongAnswerScreen() {
        viewModel.gifActive = true
        val randomWrongGif = RandomGifProvider().randomWrongGif
        viewModel.gifActive = true
        viewModel.correctAnswer = false
        binding.tvSolution.text = "Wrong!"
        binding.gifContainer.setImageResource(randomWrongGif.shuffled().random())
        binding.gifContainer.isVisible = true
        binding.answersContainer.isGone = true
        binding.tvScript.isGone = true
        binding.tvCounter.isGone = true
        binding.btNext.isEnabled = true
        binding.tvPointsNumbers.text = viewModel.userPoints.value.toString()
        viewModel.resetCounter()
    }

    private fun setCorrectAnswerScreen() {
        viewModel.gifActive = true
        val randomCorrectGif = RandomGifProvider().randomCorrectGif
        viewModel.gifActive = true
        viewModel.correctAnswer = true
        binding.tvSolution.text = "Correct!"
        binding.gifContainer.setImageResource(randomCorrectGif.shuffled().random())
        binding.gifContainer.isVisible = true
        binding.answersContainer.isGone = true
        binding.tvScript.isGone = true
        binding.tvCounter.isGone = true
        binding.btNext.isEnabled = true
        binding.tvPointsNumbers.text = viewModel.userPoints.value.toString()
        viewModel.resetCounter()
    }


    private fun initCollectors() {

        lifecycleScope.launch {
            lifecycleScope.launch {
                launch(Dispatchers.IO) {

                    viewModel.userPoints.collectLatest {
                        binding.tvPointsNumbers.text = it.toString()
                    }
                }
                binding.answersContainer.visibility = INVISIBLE
                getScriptText()
            }
        }
    }

    private fun getScriptText() {
        lifecycleScope.launch {
            launch {
                viewModel.list.collectLatest { scriptList ->
                    binding.tvScript.text =
                        scriptList.filter { it.contains(":") }.drop(1).joinToString("\n\n")
                            .replace("%", "")
                    if (binding.tvScript.text.isBlank()) {
                        launch {
                            viewModel.countingJob?.cancel()
                            viewModel.getNewScript()
                        }.join()
                    }
                    correctTitle = scriptList.firstOrNull().toString()
                    if (correctTitle.contains(":") || correctTitle.length > 20) {

                        launch {
                            viewModel.countingJob?.cancel()
                            viewModel.getNewScript()
                        }.join()

                        correctTitle = scriptList.firstOrNull().toString()
                    }
                    if (correctTitle.contains(":") || correctTitle.length > 20) {
                        println("Put episode with this line in blacklist:")
                        println(correctTitle)
                    }
                    viewModel.titlesList.collectLatest { titlesList ->
                        val editList = titlesList.shuffled().take(3).toMutableList()
                        if (titlesList.size > 2) {
                            if (correctTitle != "null") {
                                editList.add(correctTitle)
                                binding.tvAnswer1.text = editList.random()
                                editList.remove(binding.tvAnswer1.text)
                                binding.tvAnswer2.text = editList.random()
                                editList.remove(binding.tvAnswer2.text)
                                binding.tvAnswer3.text = editList.random()
                                editList.remove(binding.tvAnswer3.text)
                                binding.tvAnswer4.text = editList.random()
                                editList.remove(binding.tvAnswer4.text)
                            } else editList.add("")
                            println("correcttitle: $correctTitle")
                            binding.progressBar.isVisible = false
                            delay(200)
                            if (!viewModel.gifActive && viewModel.timer.value != 0)
                                binding.answersContainer.isVisible = true
                        }
                    }

                }
            }
        }
    }
}