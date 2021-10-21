package com.scientisthamsterssofiandjohn.samoyedtv.view.ui.home.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.scientisthamsterssofiandjohn.samoyedtv.databinding.FragmentPopularBinding
import com.scientisthamsterssofiandjohn.samoyedtv.databinding.LoadingViewLayoutBinding
import com.scientisthamsterssofiandjohn.samoyedtv.domain.pagination.PaginationState
import com.scientisthamsterssofiandjohn.samoyedtv.view.ui.home.adapter.movieInteractionListener

class LoadingViewHolder(private val binding: LoadingViewLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {

        fun bind(status: PaginationState?, listener: movieInteractionListener) {
            hideView()
            setVisibleRightViews(status)
            binding.btnRetry.setOnClickListener { listener.onClickRetry() }
        }

    private fun hideView() {
        binding.tvErrorMessage.visibility = View.GONE
        binding.btnRetry.visibility = View.GONE
    }

    private fun setVisibleRightViews(paginationState: PaginationState?) {
        when (paginationState) {
            PaginationState.ERROR -> {
                binding.tvErrorMessage.visibility = View.VISIBLE
                binding.btnRetry.visibility = View.VISIBLE
            }
        }
    }
}