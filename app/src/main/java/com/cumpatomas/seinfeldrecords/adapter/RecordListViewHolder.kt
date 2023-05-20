package com.cumpatomas.seinfeldrecords.adapter

import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.cumpatomas.seinfeldrecords.data.model.CharRecord
import com.cumpatomas.seinfeldrecords.databinding.ItemRecordListBinding
import com.cumpatomas.seinfeldrecords.ui.charrecordlist.CharRecordListFragmentArgs

class RecordListViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val binding = ItemRecordListBinding.bind(view)


//    val youTubePlayerView: YouTubePlayerView = binding.youtubePlayerView


    fun display(record: CharRecord, onClickListener: (recordId: Int, videoId: String?) -> Unit) {
        binding.tvRecordName.text = record.name
        binding.tvRecordDescription.text = record.description

        Glide.with(binding.ivRecordPreview.context) // el contexto lo sacamos de la variable charPhoto.context
            .load(record.preview)
            .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(15)))// Para redondear esquinas y recortar foto al centro!
            .into(binding.ivRecordPreview)

        itemView.setOnClickListener {
            onClickListener(record.id ?: 0, record.videoid)


        }


    }
}