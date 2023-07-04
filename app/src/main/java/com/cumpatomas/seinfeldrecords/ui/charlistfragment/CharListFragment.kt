package com.cumpatomas.seinfeldrecords.ui.charlistfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieDrawable
import com.cumpatomas.seinfeldrecords.R
import com.cumpatomas.seinfeldrecords.adapter.CharListFragmentAdapter
import com.cumpatomas.seinfeldrecords.data.model.SeinfeldChar
import com.cumpatomas.seinfeldrecords.databinding.CharListFragmentBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CharListFragment : Fragment() {
    private val viewmodel: CharListFragmentViewModel by viewModels()
    private val adapter = CharListFragmentAdapter()
    private var _binding: CharListFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CharListFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.tvLoading.isGone = true
        initListeners()
        initCollectors()
        initRecyclerView()
        setLottieAnimation()
    }

    private fun setLottieAnimation() {
        binding.tvLoading.setAnimation(R.raw.loading_yellow_white)
        binding.tvLoading.repeatMode = LottieDrawable.RESTART
        binding.tvLoading.bringToFront()
    }

    private fun initListeners() {
        adapter.onItemClickListener = {
            val action: NavDirections =
                CharListFragmentDirections.actionCharListFragmentToCharGesturesFragment("Kramer")
            findNavController().navigate(action)
        }
    }

    private fun initCollectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewmodel.charList.collectLatest { list ->
                        updateList(list)
                    }
                }

                launch {
                    viewmodel.viewState.collectLatest { state ->
                        updateUiState(state)
                    }
                }

                launch {
                    viewmodel.charRecords.collectLatest { records ->
                        adapter.charRecords = records
                    }
                }
            }
        }
    }

    private fun updateList(list: List<SeinfeldChar>) {
        adapter.setList(list)
    }

    private fun initRecyclerView() {
        val recyclerView = binding.rvRecyclerFragment3// encontramos el Recycler del Main LAYOUT xml
        recyclerView.layoutManager =
            LinearLayoutManager(context) // si cambiamos el Manager aqui podriamos hacer listados de GRID u otro tipo! Investigar!
        recyclerView.adapter = this.adapter
    }

    private fun updateUiState(state: CharListViewState) {
        binding.tvLoading.isVisible = state.loading
    }
}