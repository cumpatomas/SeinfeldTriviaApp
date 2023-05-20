package com.cumpatomas.seinfeldrecords

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cumpatomas.seinfeldrecords.adapter.CharListFragmentAdapter
import com.cumpatomas.seinfeldrecords.databinding.Fragment4Binding

class Fragment4: Fragment() {

    private var _binding: Fragment4Binding? = null
    private val binding get() = _binding!!
    private val adapter = CharListFragmentAdapter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = Fragment4Binding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecyclerView()

    }


    private fun initRecyclerView() {
        val recyclerView = binding.rvRecyclerFragment4// encontramos el Recycler del LAYOUT xml
        recyclerView.layoutManager = LinearLayoutManager(context) // si cambiamos el Manager aqui podriamos hacer listados de GRID u otro tipo! Investigar!
        recyclerView.adapter = this.adapter
    }

}