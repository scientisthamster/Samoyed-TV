package com.scientisthamsterssofiandjohn.samoyedtv.domain.repository

import androidx.lifecycle.LiveData
import com.scientisthamsterssofiandjohn.samoyedtv.data.local.MovieDao
import com.scientisthamsterssofiandjohn.samoyedtv.data.remote.MovieApi
import com.scientisthamsterssofiandjohn.samoyedtv.domain.model.MovieDetail
import com.scientisthamsterssofiandjohn.samoyedtv.domain.model.MovieQuery
import com.scientisthamsterssofiandjohn.samoyedtv.domain.model.MovieResult
import retrofit2.Response
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDao: MovieDao
) {

    suspend fun fetchMovies(map: Map<String, String>, queryTeg: QUERYTAG): Response<MovieQuery> {
        return when (queryTeg) {
            QUERYTAG.SEARCH -> movieApi.searchMovie(map)
            QUERYTAG.POPULAR -> movieApi.popularMovie(map)
            QUERYTAG.TRENDING -> movieApi.trendingMovie(map["time_window"] ?: error("week"), map)
            QUERYTAG.TOP_RATE -> movieApi.topRateMovie(map)
        }
    }

    suspend fun movieDetail(movieId: Long, query: String): Response<MovieDetail> {
        return movieApi.movieDetail(movieId, query)
    }

    fun allFavoriteMovie(): LiveData<List<MovieResult>> {
        return movieDao.allFavorite()
    }

    suspend fun insert(movieResult: MovieResult) {
        return movieDao.insertFavorite(movieResult)
    }

    suspend fun update(movieResult: MovieResult) {
        return movieDao.updateFavorite(movieResult)
    }

    suspend fun delete(movieResult: MovieResult) {
        return movieDao.deleteFavorite(movieResult)
    }

    fun existAsFavorite(id: String): LiveData<List<MovieResult>> {
        return movieDao.existAsFavorite(id)
    }

    enum class QUERYTAG {
        SEARCH, POPULAR, TRENDING, TOP_RATE
    }
}