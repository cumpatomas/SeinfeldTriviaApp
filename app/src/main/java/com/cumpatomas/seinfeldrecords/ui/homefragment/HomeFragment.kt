package com.cumpatomas.seinfeldrecords

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.cumpatomas.seinfeldrecords.databinding.HomeFragmentBinding
import com.cumpatomas.seinfeldrecords.domain.ScrapScripts
import com.cumpatomas.seinfeldrecords.ui.homefragment.HomeFragmentViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

class Fragment2 : Fragment() {
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


    }

    private fun initListeners() {
        binding.btNext.setOnClickListener {
            binding.progressBar.isVisible = true
            lifecycleScope.launch(IO) {
                launch {

                    viewModel.getNewScript()
                }.join()
            }
            binding.progressBar.isVisible = false
        }

        binding.tvAnswer1.setOnClickListener {
            binding.tvSolution.text = (if (binding.tvAnswer1.text == correctTitle)
                "correct!"
            else
                "you stink!")
          }
        binding.tvAnswer2.setOnClickListener {
            binding.tvSolution.text = (if (binding.tvAnswer2.text == correctTitle)
                "correct!"
            else
                "you stink!")
        }
        binding.tvAnswer3.setOnClickListener {
            binding.tvSolution.text =  (if (binding.tvAnswer3.text == correctTitle)
                "correct!"
            else
                "you stink!")
        }
        binding.tvAnswer4.setOnClickListener {
            binding.tvSolution.text = (if (binding.tvAnswer4.text == correctTitle)
                "correct!"
            else
                "you stink!")
        }
    }


    @SuppressLint("SetTextI18n")
    private fun initCollectors() {

        lifecycleScope.launch {


            launch {


                viewModel.list.collectLatest { scriptList ->
                    binding.tvScript.text = scriptList.drop(1).take(6).joinToString("\n\n") + "..."
                    correctTitle = scriptList.firstOrNull().toString()
                    binding.tvEpisodeTitle.text =
                        "Select the correct episode title to which these lines belong:"
                    println("correct Title: $correctTitle")

                    viewModel.titlesList.collectLatest { titlesList ->
                        if (titlesList.size > 4) {
                            val list = titlesList.take(3).toMutableList()
                            list.add(correctTitle)
                            binding.tvAnswer1.text = list.shuffled().random()
                            list.remove(binding.tvAnswer1.text)
                            binding.tvAnswer2.text = list.shuffled().random()
                            list.remove(binding.tvAnswer2.text)
                            binding.tvAnswer3.text = list.shuffled().random()
                            list.remove(binding.tvAnswer3.text)
                            binding.tvAnswer4.text = list.shuffled().random()
                            binding.progressBar.isVisible = false
                        }
                    }
                }
            }
        }
    }
}