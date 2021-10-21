package com.scientisthamsterssofiandjohn.samoyedtv.view.ui.home.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.scientisthamsterssofiandjohn.samoyedtv.databinding.SingleMoviePosterListPageBinding
import com.scientisthamsterssofiandjohn.samoyedtv.domain.model.MovieResult
import com.scientisthamsterssofiandjohn.samoyedtv.util.Constants.Companion.BASE_IMAGE_URL_w500_API
import com.scientisthamsterssofiandjohn.samoyedtv.view.ui.home.adapter.movieInteractionListener

class MovieViewHolder(private val binding: SingleMoviePosterListPageBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(
        movieResult: MovieResult?,
        listener: movieInteractionListener
    ) {
        if (movieResult != null) {
            binding.tvTitle.text = movieResult.title
            val average = movieResult.vote_average / 2.0
            binding.ratingFilm.rating = average.toFloat()
            Glide.with(itemView).load(BASE_IMAGE_URL_w500_API + movieResult.poster_path)
                .into(binding.imPoster)
            itemView.setOnClickListener { listener.onMovieClick(movieResult, adapterPosition) }
        }
    }
}