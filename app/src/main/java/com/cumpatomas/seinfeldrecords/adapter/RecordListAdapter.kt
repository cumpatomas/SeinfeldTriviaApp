package com.cumpatomas.seinfeldrecords.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cumpatomas.seinfeldrecords.R
import com.cumpatomas.seinfeldrecords.data.model.CharRecord

class RecordListAdapter: RecyclerView.Adapter<RecordListViewHolder>() {

    private var  recordList = mutableListOf<CharRecord>()

    var onItemClickListener: (recordId: Int, videoId: String?) -> Unit = {_, _ -> }

    fun setList(list: List<CharRecord>) {
        recordList = list.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return RecordListViewHolder(layoutInflater.inflate(R.layout.item_record_list, parent, false))

    }

    override fun onBindViewHolder(holder: RecordListViewHolder, position: Int) {
        val item = recordList[position]
        holder.display(item, onItemClickListener)

    }

    override fun getItemCount(): Int = recordList.size


}