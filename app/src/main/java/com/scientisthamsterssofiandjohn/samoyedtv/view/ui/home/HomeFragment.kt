package com.scientisthamsterssofiandjohn.samoyedtv.view.ui.home

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.scientisthamsterssofiandjohn.samoyedtv.R
import com.scientisthamsterssofiandjohn.samoyedtv.databinding.FragmentHomeBinding
import com.scientisthamsterssofiandjohn.samoyedtv.domain.model.MovieResult
import com.scientisthamsterssofiandjohn.samoyedtv.domain.pagination.PaginationState
import com.scientisthamsterssofiandjohn.samoyedtv.view.customview.EmptyView
import com.scientisthamsterssofiandjohn.samoyedtv.view.ui.home.adapter.MoviePagedListAdapter
import com.scientisthamsterssofiandjohn.samoyedtv.view.ui.home.adapter.movieInteractionListener
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class HomeFragment : Fragment(R.layout.fragment_home), SwipeRefreshLayout.OnRefreshListener,
    movieInteractionListener {

    private lateinit var binding: FragmentHomeBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var pagedAdapter: MoviePagedListAdapter

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel =
            ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        initUI()
        obsStates()
        obsData()
    }

    private fun obsStates() {
        homeViewModel.paginationState?.observe(viewLifecycleOwner, Observer {
            updateUIPaginationState(it)
            pagedAdapter.updatePaginationState(it)
        })
    }

    private fun initUI() {
        binding.emptyView.emptyStateType(EmptyView.STATETYPE.NOERROR, null)
        binding.swipe.setOnRefreshListener(this)
        pagedAdapter = MoviePagedListAdapter(this)
        binding.homeList.layoutManager = gridLayoutManager()
        binding.homeList.adapter = pagedAdapter
    }

    private fun obsData() {
        homeViewModel.moviePagedLiveData.observe(viewLifecycleOwner, Observer { pagedList ->
            pagedAdapter.submitList(pagedList)
        })
    }

    private fun gridLayoutManager(): RecyclerView.LayoutManager? {
        val mLayoutManager = GridLayoutManager(activity, 2)
        mLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (pagedAdapter.getItemViewType(position)) {
                    R.layout.loading_view_layout -> mLayoutManager.spanCount
                    else -> 1
                }
            }
        }
        return mLayoutManager
    }

    override fun onClickRetry() {
        homeViewModel.refreshFailedRequest()
    }

    override fun onMovieClick(movieResult: MovieResult, pos: Int) {
        val bundle = bundleOf("movie" to movieResult)
        findNavController().navigate(R.id.action_homeFragment_to_detailFragment, bundle)
    }

    private fun updateUIPaginationState(paginationState: PaginationState?) {
        when (paginationState) {
            PaginationState.LOADING -> {
                binding.swipe.isRefreshing = true
            }
            PaginationState.EMPTY -> {
                binding.swipe.isRefreshing = false
                if (pagedAdapter.currentList.isNullOrEmpty()) {
                    binding.emptyView.emptyStateType(EmptyView.STATETYPE.EMPTY, null)
                }
            }
            PaginationState.ERROR -> {
                binding.swipe.isRefreshing = false
                if (pagedAdapter.currentList.isNullOrEmpty()) {
                    binding.emptyView.emptyStateType(
                        EmptyView.STATETYPE.CONNECTION,
                        View.OnClickListener {
                            onRefresh()
                        })
                }
            }
            PaginationState.DONE -> {
                binding.swipe.isRefreshing = false
                binding.emptyView.emptyStateType(EmptyView.STATETYPE.NOERROR, null)
            }
        }
    }

    override fun onRefresh() {
        homeViewModel.refreshAllList()
    }
}