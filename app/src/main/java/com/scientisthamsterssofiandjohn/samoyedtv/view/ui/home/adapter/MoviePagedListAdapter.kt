package com.scientisthamsterssofiandjohn.samoyedtv.view.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.scientisthamsterssofiandjohn.samoyedtv.R
import com.scientisthamsterssofiandjohn.samoyedtv.databinding.LoadingViewLayoutBinding
import com.scientisthamsterssofiandjohn.samoyedtv.databinding.SingleMoviePosterListPageBinding
import com.scientisthamsterssofiandjohn.samoyedtv.domain.model.MovieResult
import com.scientisthamsterssofiandjohn.samoyedtv.domain.pagination.PaginationState
import com.scientisthamsterssofiandjohn.samoyedtv.view.ui.home.viewholder.LoadingViewHolder
import com.scientisthamsterssofiandjohn.samoyedtv.view.ui.home.viewholder.MovieViewHolder

class MoviePagedListAdapter(private val listener: movieInteractionListener) :
    PagedListAdapter<MovieResult, RecyclerView.ViewHolder>(diffUtilCallback) {

    companion object {
        private val diffUtilCallback = object : DiffUtil.ItemCallback<MovieResult>() {
            override fun areItemsTheSame(oldItem: MovieResult, newItem: MovieResult): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: MovieResult,
                newItem: MovieResult
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    private var state: PaginationState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.single_movie_poster_list_page -> {
                MovieViewHolder(
                    SingleMoviePosterListPageBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            R.layout.loading_view_layout -> {
                LoadingViewHolder(
                    LoadingViewLayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> throw IllegalArgumentException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.single_movie_poster_list_page -> (holder as MovieViewHolder).bind(
                getItem(position),
                listener
            )
            R.layout.loading_view_layout -> (holder as LoadingViewHolder).bind(state, listener)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) {
            R.layout.single_movie_poster_list_page
        } else {
            R.layout.loading_view_layout
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (state == PaginationState.LOADING || state == PaginationState.ERROR)
    }

    fun updatePaginationState(newState: PaginationState) {
        this.state = newState
        if (newState != PaginationState.LOADING) {
            notifyDataSetChanged()
        }
    }
}