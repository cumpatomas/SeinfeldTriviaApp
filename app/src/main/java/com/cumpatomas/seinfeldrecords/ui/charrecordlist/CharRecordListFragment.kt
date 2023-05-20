package com.cumpatomas.seinfeldrecords.ui.charrecordlist

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.cumpatomas.seinfeldrecords.adapter.RecordListAdapter
import com.cumpatomas.seinfeldrecords.databinding.RecordListFragmentBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgument
import com.cumpatomas.seinfeldrecords.R

class CharRecordListFragment: Fragment() {

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

    private fun initListeners() {
        val totalRecords = getString(R.string.char_total_records)
        val charSelected = navArgs.selectedCharRecord.substring(0, navArgs.selectedCharRecord.indexOf(' ')); // formula to get the first name only

        binding.tvNameAndRecords.text = "$charSelected's $totalRecords"

        adapter.onItemClickListener = { id, videoId ->

            val action = CharRecordListFragmentDirections.actionCharRecordListFragmentToVideoPlayerFragment2(id)
            findNavController().navigate(action)

        }
    }

    private fun receiveArguments() {
        viewmodel.receiveArguments(navArgs.selectedCharRecord)
    }
}