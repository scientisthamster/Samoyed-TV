package com.scientisthamsterssofiandjohn.samoyedtv.view.ui.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.scientisthamsterssofiandjohn.samoyedtv.databinding.ActorViewLayoutBinding
import com.scientisthamsterssofiandjohn.samoyedtv.domain.model.Actor
import com.scientisthamsterssofiandjohn.samoyedtv.view.ui.detail.viewholder.ActorViewHolder

class ActorListAdapter : RecyclerView.Adapter<ActorViewHolder>() {

    private var actors: List<Actor> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {
        return ActorViewHolder(
            ActorViewLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        val actor = actors[position]
        holder.bind(actor)
    }

    override fun getItemCount(): Int = actors.size

    fun submitList(listActors: List<Actor>) {
        this.actors = listActors
        notifyDataSetChanged()
    }
}