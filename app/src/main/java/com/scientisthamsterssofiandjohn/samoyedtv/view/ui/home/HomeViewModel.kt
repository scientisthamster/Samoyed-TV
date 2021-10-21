package com.scientisthamsterssofiandjohn.samoyedtv.view.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import com.scientisthamsterssofiandjohn.samoyedtv.domain.pagination.MovieDataSourceFactory
import com.scientisthamsterssofiandjohn.samoyedtv.domain.pagination.PaginationState
import com.scientisthamsterssofiandjohn.samoyedtv.domain.repository.MovieRepository
import com.scientisthamsterssofiandjohn.samoyedtv.util.QueryHelper
import com.scientisthamsterssofiandjohn.samoyedtv.view.base.BaseViewModel
import javax.inject.Inject

class HomeViewModel @Inject constructor(repository: MovieRepository) : BaseViewModel() {

    private var movieDataSourceFactory: MovieDataSourceFactory =
        MovieDataSourceFactory(
            MovieRepository.QUERYTAG.TRENDING,
            QueryHelper.trendingMovies(),
            repository
        )

    val moviePagedLiveData = pagedConfig.let {
        LivePagedListBuilder(movieDataSourceFactory,
            it
        ).build()
    }

    val paginationState: LiveData<PaginationState>? =
        Transformations.switchMap(movieDataSourceFactory.movieDataSourceLiveData) { it.getPaginationState() }

    /**
     * Retry possible last paged request
     */
    fun refreshFailedRequest() =
        movieDataSourceFactory.getSource()?.retryFailedQuery()

    /**
     * Refreshes all list after an issue
     */
    fun refreshAllList() =
        movieDataSourceFactory.getSource()?.refresh()
}