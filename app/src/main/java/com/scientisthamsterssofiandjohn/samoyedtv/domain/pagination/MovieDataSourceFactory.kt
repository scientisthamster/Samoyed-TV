package com.scientisthamsterssofiandjohn.samoyedtv.domain.pagination

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.scientisthamsterssofiandjohn.samoyedtv.domain.model.MovieResult
import com.scientisthamsterssofiandjohn.samoyedtv.domain.repository.MovieRepository

class MovieDataSourceFactory(
    private var querytag: MovieRepository.QUERYTAG,
    private var queryParams: HashMap<String, String>,
    private val repository: MovieRepository
) : DataSource.Factory<Int, MovieResult>() {

    val movieDataSourceLiveData = MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, MovieResult> {
        val movieDataSource = MovieDataSource(querytag, queryParams, repository)
        movieDataSourceLiveData.postValue(movieDataSource)
        return movieDataSource
    }

    fun getSource() = movieDataSourceLiveData.value

    fun getLastQueryParams() = queryParams

    fun getLastQueryTag() = querytag

    fun updateQueryParams(queryParams: HashMap<String, String>, newQueryTag: MovieRepository.QUERYTAG) {
        this.queryParams = queryParams
        this.querytag = newQueryTag
        movieDataSourceLiveData.value?.refresh()
    }
}