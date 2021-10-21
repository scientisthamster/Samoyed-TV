package com.scientisthamsterssofiandjohn.samoyedtv.view.ui.detail.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.scientisthamsterssofiandjohn.samoyedtv.databinding.ActorViewLayoutBinding
import com.scientisthamsterssofiandjohn.samoyedtv.domain.model.Actor
import com.scientisthamsterssofiandjohn.samoyedtv.util.Constants.Companion.BASE_IMAGE_URL_w500_API

class ActorViewHolder(private val binding: ActorViewLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(actor: Actor?) {
        if (actor != null) {
            binding.tvActorValue.text = actor.name
            Glide.with(itemView).load(BASE_IMAGE_URL_w500_API + actor.profile_path).into(binding.ivActor)
        }
    }
}