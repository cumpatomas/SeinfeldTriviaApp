package com.cumpatomas.seinfeldrecords.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cumpatomas.seinfeldrecords.data.model.SeinfeldChar
import com.cumpatomas.seinfeldrecords.R
import com.cumpatomas.seinfeldrecords.data.model.CharRecord

class CharListFragmentAdapter(): RecyclerView.Adapter<CharListFragmentViewHolder>() {

    private var  charList = mutableListOf<SeinfeldChar>()
    lateinit var charRecords: List<CharRecord>


    var onItemClickListener: (SeinfeldChar) -> Unit = {}

    fun setList(list: List<SeinfeldChar>) {
        charList = list.toMutableList()
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharListFragmentViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context) // aca el contexto lo sacamos de "parent"
        return CharListFragmentViewHolder(layoutInflater.inflate(R.layout.list_item_character, parent, false))

    }

    override fun onBindViewHolder(holder: CharListFragmentViewHolder, position: Int) {
        val item = charList[position]
        val records = charRecords.filter { it.mainChar == item.name }.size.toString()
        holder.display(item, onItemClickListener, records)
    }


    override fun getItemCount(): Int = charList.size



}

