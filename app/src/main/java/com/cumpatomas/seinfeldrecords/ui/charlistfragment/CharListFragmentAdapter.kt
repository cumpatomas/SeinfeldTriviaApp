package com.cumpatomas.seinfeldrecords.ui.charlistfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cumpatomas.seinfeldrecords.data.model.SeinfeldChar
import com.cumpatomas.seinfeldrecords.R
import com.cumpatomas.seinfeldrecords.data.model.CharGestures

class CharListFragmentAdapter(): RecyclerView.Adapter<CharListFragmentViewHolder>() {

    private var  charList = mutableListOf<SeinfeldChar>()
    lateinit var charGestures: List<CharGestures>


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
        holder.display(item, onItemClickListener, item.completed)
    }


    override fun getItemCount(): Int = charList.size



}

