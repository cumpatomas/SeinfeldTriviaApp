package com.cumpatomas.seinfeldrecords.ui.charlistfragment

import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.cumpatomas.seinfeldrecords.R
import com.cumpatomas.seinfeldrecords.data.model.SeinfeldChar
import com.cumpatomas.seinfeldrecords.databinding.ListItemCharacterBinding

class CharListFragmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ListItemCharacterBinding.bind(view)
    val charName = view.findViewById<TextView>(R.id.tvCharName)
    val charSpecs = view.findViewById<TextView>(R.id.tvCharSpecs)
    val charRelation = view.findViewById<TextView>(R.id.tvCharRelation)
    val charPhoto = view.findViewById<ImageView>(R.id.ivCharacter)
    val completedPhoto = view.findViewById<ImageView>(R.id.ivComplete)
    val playButton = view.findViewById<ImageView>(R.id.ivPlay)
    val episodesNumber = view.findViewById<TextView>(R.id.tvEpisodesNumber)

    fun display(
        char: SeinfeldChar,
        onItemClickListener: (SeinfeldChar) -> Unit,
        completed: Boolean
    ) {
        charName.text = char.name
        charSpecs.text = char.specs
        charRelation.text = char.relationWithJerry

        episodesNumber.text = when (char.shortName) {
            "Jerry" -> "180"
            "Elaine" -> "177"
            "George" -> "179"
            "Kramer" -> "178"
            "Newman" -> "48"
            "Estelle" -> "29"
            "Frank" -> "29"
            "Puddy" -> "11"
            else -> ""
        }

        Glide.with(charPhoto.context) // el contexto lo sacamos de la variable charPhoto.context
            .load(char.photo)
            .apply(
                RequestOptions().transform(
                    CenterCrop(),
                    RoundedCorners(15)
                )
            )// Para redondear esquinas y recortar foto al centro!
            .into(charPhoto)

        if (completed) {
            completedPhoto.isVisible = true
            playButton.isGone = true
        }

        itemView.setOnClickListener {
            onItemClickListener(char)
        }
        setAnimation()
    }

    private fun setAnimation() {
        val animation = AnimationUtils.loadAnimation(binding.root.context, R.anim.slide_in_right)
        itemView.startAnimation(animation)
    }
}