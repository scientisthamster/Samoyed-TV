package com.scientisthamsterssofiandjohn.samoyedtv.view.ui.favorite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.scientisthamsterssofiandjohn.samoyedtv.databinding.SingleMoviePosterListPageBinding
import com.scientisthamsterssofiandjohn.samoyedtv.domain.model.MovieResult
import com.scientisthamsterssofiandjohn.samoyedtv.view.ui.home.adapter.movieInteractionListener
import com.scientisthamsterssofiandjohn.samoyedtv.view.ui.home.viewholder.MovieViewHolder

class FavoriteListAdapter(private val listener: movieInteractionListener) :
    RecyclerView.Adapter<MovieViewHolder>() {

    private var movies: List<MovieResult> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            SingleMoviePosterListPageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie, listener)
    }

    override fun getItemCount(): Int = movies.size

    fun submitList(submitList: List<MovieResult>) {
        this.movies = submitList
        notifyDataSetChanged()
    }
}