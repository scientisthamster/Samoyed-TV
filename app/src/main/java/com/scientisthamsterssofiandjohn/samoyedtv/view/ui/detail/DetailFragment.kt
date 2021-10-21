package com.scientisthamsterssofiandjohn.samoyedtv.view.ui.detail

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.scientisthamsterssofiandjohn.samoyedtv.R
import com.scientisthamsterssofiandjohn.samoyedtv.data.Resource
import com.scientisthamsterssofiandjohn.samoyedtv.databinding.DetailMovieContentLayoutBinding
import com.scientisthamsterssofiandjohn.samoyedtv.databinding.FragmentDetailBinding
import com.scientisthamsterssofiandjohn.samoyedtv.databinding.OverlapLoadingViewLayoutBinding
import com.scientisthamsterssofiandjohn.samoyedtv.domain.model.MovieDetail
import com.scientisthamsterssofiandjohn.samoyedtv.domain.model.MovieResult
import com.scientisthamsterssofiandjohn.samoyedtv.util.Constants.Companion.BASE_IMAGE_URL_ORIGINAL_API
import com.scientisthamsterssofiandjohn.samoyedtv.util.Constants.Companion.BASE_IMAGE_URL_w500_API
import com.scientisthamsterssofiandjohn.samoyedtv.view.customview.OverlapLoadingView
import com.scientisthamsterssofiandjohn.samoyedtv.view.ui.detail.adapter.ActorListAdapter
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class DetailFragment : Fragment(R.layout.fragment_detail), View.OnClickListener {

    private lateinit var binding: FragmentDetailBinding

    private var snackbar: Snackbar? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: DetailViewModel
    private lateinit var actorsAdapter: ActorListAdapter

    private var movieResult: MovieResult? = null
    private var movieIsFavorite = false

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DetailViewModel::class.java)
        movieResult = requireArguments().getParcelable("movie")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailBinding.bind(view)
        updateCachedUI()
        obsState()
        obsData()
        viewModel.loadMovieDetail(movieResult!!.id)
    }

    private fun obsData() {
        viewModel.getMovieDetail().observe(viewLifecycleOwner, Observer {
            updateUI(it)
        })
        viewModel.movieIsFavorite(movieResult!!.id.toString())
            .observe(viewLifecycleOwner, Observer {
                changeFavoriteIcon(it.isNotEmpty())
            })
    }

    private fun changeFavoriteIcon(isFavorite: Boolean) {
        movieIsFavorite = isFavorite
        binding.ivFavorite.setImageResource(if (isFavorite) R.drawable.saved2 else R.drawable.saved)
    }

    private fun updateUI(resource: Resource<MovieDetail>?) {
        if (resource?.data != null) {
            if (resource.data.genres != null) {
                val genres = resource.data.genres.joinToString { it.name }
                binding.tvGnreValue.text = genres
            }
            if (resource.data.runtime != null) {
                val hours = resource.data.runtime!! / 60
                val min = resource.data.runtime % 60
                binding.tvDuration.text = String.format("%sh %smin", hours, min)
                binding.tvDuration.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.ic_schedule,
                    0, 0, 0
                )
                binding.tvDuration.compoundDrawablePadding = 5
            }
            if (!resource.data.overview.isNullOrEmpty()) {
                binding.tvDescriptionValue.apply {
                    visibility = View.VISIBLE
                    text = resource.data.overview
                }
                binding.tvDescriptionTitle.visibility = View.VISIBLE
            } else {
                binding.tvDescriptionValue.visibility = View.GONE
                binding.tvDescriptionTitle.visibility = View.GONE
            }
            actorsAdapter.submitList(resource.data.credits?.cast ?: listOf())
            binding.loadingView.loadingStateType(OverlapLoadingView.STATETYPE.DONE)
            binding.contentDescription.visibility = View.VISIBLE
        } else {
            binding.loadingView.loadingStateType(OverlapLoadingView.STATETYPE.ERROR)
            snackbar =
                Snackbar.make(binding.nestedDetail, "Lost connection", Snackbar.LENGTH_INDEFINITE)
            snackbar!!.setAction(R.string.retry) {
                snackbar!!.dismiss()
                Handler().postDelayed({
                    viewModel.loadMovieDetail(movieResult!!.id)
                }, 250)
            }
            snackbar!!.show()
        }
    }

    private fun obsState() {
        viewModel.getProgressState().observe(viewLifecycleOwner, Observer { loading ->
            if (loading) {
                binding.loadingView.loadingStateType(OverlapLoadingView.STATETYPE.LOADING)
            }
        })
    }

    private fun updateCachedUI() {
        binding.ivFavorite.setOnClickListener(this)
        Glide.with(requireContext())
            .load(BASE_IMAGE_URL_ORIGINAL_API + movieResult!!.backdrop_path)
            .error(R.drawable.spare_logo)
            .into(binding.ivBackdrop)
        Glide.with(requireContext())
            .load(BASE_IMAGE_URL_w500_API + movieResult!!.poster_path)
            .error(R.drawable.spare_logo)
            .into(binding.ivPoster)
        binding.tvMovieTitleValue.text = movieResult!!.title
        if (movieResult!!.title.length > 10) {
            binding.tvMovieTitleValue.isSelected = true
        }
        val voteAverage = movieResult!!.vote_average / 2.0
        binding.tvVoteAverage.text = voteAverage.toString()
        binding.ratingFilm.rating = voteAverage.toFloat()
        actorsAdapter = ActorListAdapter()
        binding.rvActors.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = actorsAdapter
        }
    }

    override fun onDestroyView() {
        if (snackbar != null && snackbar!!.isShown) {
            snackbar!!.dismiss()
        }
        super.onDestroyView()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ivFavorite ->
                viewModel.saveFavorite(movieResult!!, !movieIsFavorite)
        }
    }
}