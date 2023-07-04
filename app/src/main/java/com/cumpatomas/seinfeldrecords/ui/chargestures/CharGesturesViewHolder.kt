package com.cumpatomas.seinfeldrecords.ui.chargestures

import android.view.View
import android.view.animation.AnimationUtils
import androidx.compose.ui.graphics.Shadow
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.cumpatomas.seinfeldrecords.R
import com.cumpatomas.seinfeldrecords.data.model.CharGestures
import com.cumpatomas.seinfeldrecords.databinding.CharGesturesItemBinding

class CharGesturesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = CharGesturesItemBinding.bind(view)

    fun display(item: CharGestures, onItemClickListener: (CharGestures) -> Unit) {
        Glide.with(binding.ivCharGestureItemImage.context) // el contexto lo sacamos de la variable charPhoto.context
            .load(item.photoLink)
            .apply(
                RequestOptions().transform(
                    CircleCrop(),
                )
            )
            .into(binding.ivCharGestureItemImage)
        setAnimation()
    }

    private fun setAnimation() {
        val animation = AnimationUtils.loadAnimation(binding.root.context, R.anim.slide_from_bottom)
        itemView.startAnimation(animation)
    }
}