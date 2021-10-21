package com.scientisthamsterssofiandjohn.samoyedtv.view.ui.favorite

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.scientisthamsterssofiandjohn.samoyedtv.R
import com.scientisthamsterssofiandjohn.samoyedtv.databinding.FragmentFavoriteBinding
import com.scientisthamsterssofiandjohn.samoyedtv.domain.model.MovieResult
import com.scientisthamsterssofiandjohn.samoyedtv.view.customview.EmptyView
import com.scientisthamsterssofiandjohn.samoyedtv.view.ui.favorite.adapter.FavoriteListAdapter
import com.scientisthamsterssofiandjohn.samoyedtv.view.ui.home.adapter.movieInteractionListener
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class FavoriteFragment : Fragment(R.layout.fragment_favorite), movieInteractionListener {

    private lateinit var binding: FragmentFavoriteBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var favoriteAdapter: FavoriteListAdapter
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteViewModel =
            ViewModelProvider(this, viewModelFactory).get(FavoriteViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoriteBinding.bind(view)
        initUI()
        obsStates()
        obsData()
    }

    private fun obsData() {
        favoriteViewModel.getMovieFavorites().observe(viewLifecycleOwner, Observer { favorites ->
            if (favorites.data != null && favorites.data.isNotEmpty()) {
                favoriteAdapter.submitList(favorites.data)
            } else {
                binding.emptyView.emptyStateType(EmptyView.STATETYPE.EMPTY, null)
            }
        })
    }

    private fun obsStates() {
        favoriteViewModel.getProgressState().observe(viewLifecycleOwner, Observer { loading ->
            if (loading) {
                binding.favoriteLoading.visibility = View.VISIBLE
            } else binding.favoriteLoading.visibility = View.GONE
        })
    }

    private fun initUI() {
        binding.emptyView.emptyStateType(EmptyView.STATETYPE.NOERROR, null)
        favoriteAdapter = FavoriteListAdapter(this)
        binding.favorites.layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.favorites.adapter = favoriteAdapter
    }

    override fun onClickRetry() {
        //do nothing
    }

    override fun onMovieClick(movieResult: MovieResult, pos: Int) {
        findNavController().navigate(R.id.action_favoriteFragment_to_detailFragment, bundleOf("movie" to movieResult))
    }
}