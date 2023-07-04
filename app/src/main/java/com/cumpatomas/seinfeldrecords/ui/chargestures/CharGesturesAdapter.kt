package com.cumpatomas.seinfeldrecords.ui.chargestures

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cumpatomas.seinfeldrecords.R
import com.cumpatomas.seinfeldrecords.data.CharListProvider.Companion.charList
import com.cumpatomas.seinfeldrecords.data.model.CharGestures

class CharGesturesAdapter : RecyclerView.Adapter<CharGesturesViewHolder>() {
    private var charGesturesList = mutableListOf<CharGestures>()
    var onItemClickListener: (CharGestures) -> Unit = {}

    fun setList(list: List<CharGestures>) {
        charGesturesList = list.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharGesturesViewHolder {
        val layoutInflater =
            LayoutInflater.from(parent.context) // aca el contexto lo sacamos de "parent"
        return CharGesturesViewHolder(
            layoutInflater.inflate(
                R.layout.char_gestures_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = charGesturesList.size

    override fun onBindViewHolder(holder: CharGesturesViewHolder, position: Int) {
        val item = charGesturesList[position]
        holder.display(item, onItemClickListener)
    }
}