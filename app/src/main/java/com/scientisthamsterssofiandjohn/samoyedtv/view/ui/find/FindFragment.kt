package com.scientisthamsterssofiandjohn.samoyedtv.view.ui.find

import android.animation.LayoutTransition
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.scientisthamsterssofiandjohn.samoyedtv.R
import com.scientisthamsterssofiandjohn.samoyedtv.databinding.FragmentFindBinding
import com.scientisthamsterssofiandjohn.samoyedtv.domain.model.MovieResult
import com.scientisthamsterssofiandjohn.samoyedtv.domain.pagination.PaginationState
import com.scientisthamsterssofiandjohn.samoyedtv.view.customview.EmptyView
import com.scientisthamsterssofiandjohn.samoyedtv.view.ui.home.adapter.MoviePagedListAdapter
import com.scientisthamsterssofiandjohn.samoyedtv.view.ui.home.adapter.movieInteractionListener
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class FindFragment : Fragment(R.layout.fragment_find), SwipeRefreshLayout.OnRefreshListener,
    movieInteractionListener {

    private lateinit var binding: FragmentFindBinding

    private lateinit var searchView: SearchView

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var findViewModel: FindViewModel

    private lateinit var pagedAdapter: MoviePagedListAdapter


    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        findViewModel =
            ViewModelProvider(this, viewModelFactory).get(FindViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFindBinding.bind(view)
        initUI()
        obsStates()
        obsData()
    }

    private fun initUI() {
        binding.emptyView.emptyStateType(EmptyView.STATETYPE.NOERROR, null)
        binding.swipe.setOnRefreshListener(this)
        pagedAdapter = MoviePagedListAdapter(this)
        binding.searchList.layoutManager = gridLayoutManager()
        binding.searchList.adapter = pagedAdapter
    }

    private fun obsData() {
        findViewModel.moviePagedLiveData.observe(viewLifecycleOwner, Observer { pagedList ->
            pagedAdapter.submitList(pagedList)
        })
    }

    private fun obsStates() {
        findViewModel.paginationState?.observe(viewLifecycleOwner, Observer {
            updateUIPaginationState(it)
            pagedAdapter.updatePaginationState(it)
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
                    binding.emptyView.emptyStateType(EmptyView.STATETYPE.CONNECTION, View.OnClickListener {
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
        findViewModel.refreshAllList()
    }

    override fun onClickRetry() {
        findViewModel.refreshFailedRequest()
    }

    override fun onMovieClick(movieResult: MovieResult, pos: Int) {
        val bundle = bundleOf("movie" to movieResult)
        findNavController().navigate(R.id.action_findFragment_to_detailFragment, bundle)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.search_menu, menu)

        val searchManager =
            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager

        val menuItemSearch = menu.findItem(R.id.action_search)
        searchView = menuItemSearch.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        personalizeSearchView()
        initSearchViewListener()
    }

    private fun initSearchViewListener() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                this.callSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            private fun callSearch(query: String) {
                findViewModel.searchMovieByName(query)
            }
        })
    }

    private fun personalizeSearchView() {
        val txtSearch =
            searchView.findViewById<View>(androidx.appcompat.R.id.search_src_text) as EditText
        txtSearch.hint = getString(R.string.search)
        txtSearch.setHintTextColor(ContextCompat.getColor(requireContext(), R.color.white_40))
        txtSearch.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

        // change search close button image
        val closeButton = searchView.findViewById<ImageView>(R.id.search_close_btn)
        closeButton.setImageResource(R.drawable.ic_close)

        // Make animation transition
        val searchBar = searchView.findViewById(R.id.search_bar) as LinearLayout
        searchBar.layoutTransition = LayoutTransition()
    }
}