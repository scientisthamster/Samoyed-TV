package com.scientisthamsterssofiandjohn.samoyedtv.view.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.scientisthamsterssofiandjohn.samoyedtv.data.Resource
import com.scientisthamsterssofiandjohn.samoyedtv.data.Status
import com.scientisthamsterssofiandjohn.samoyedtv.domain.model.MovieDetail
import com.scientisthamsterssofiandjohn.samoyedtv.domain.model.MovieResult
import com.scientisthamsterssofiandjohn.samoyedtv.domain.repository.MovieRepository
import com.scientisthamsterssofiandjohn.samoyedtv.util.SingleLiveEvent
import com.scientisthamsterssofiandjohn.samoyedtv.view.base.BaseViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class DetailViewModel @Inject constructor(private val repository: MovieRepository) :
    BaseViewModel() {

    private val movieDetail: MutableLiveData<Resource<MovieDetail>> = MutableLiveData()

    private val favoriteSavedState: SingleLiveEvent<Resource<Pair<Boolean, Boolean>>> =
        SingleLiveEvent()

    fun loadMovieDetail(movieId: Long) = GlobalScope.launch(main + job) {
        progressState.postValue(true)
        try {
            val result = repository.movieDetail(movieId, "credits")
            val sublistResult = getFirstItemWithProfilePath(result)
            movieDetail.postValue(Resource(Status.SUCCESS, sublistResult, null))
        } catch (e: Exception) {
            movieDetail.postValue(Resource(Status.ERROR, null, e))
        }
        progressState.postValue(false)
    }

    private suspend fun getFirstItemWithProfilePath(
        result: Response<MovieDetail>
    ): MovieDetail? =
        withContext(default + job) {
            if (result.isSuccessful && result.body() != null) {
                val actorCurrentList = result.body()?.credits?.cast ?: arrayListOf()
                if (actorCurrentList.isNotEmpty()) {
                    val filteredList = actorCurrentList.filter {
                        it.profile_path != null
                    }
                    result.body()?.credits?.cast = filteredList
                }
                result.body()
            } else {
                null
            }
        }

    fun saveFavorite(movieLocal: MovieResult, favorite: Boolean) = GlobalScope.launch(main + job) {
        try {
            if (favorite) {
                repository.insert(movieLocal)
            } else {
                repository.delete(movieLocal)
            }
            favoriteSavedState.postValue(Resource(Status.SUCCESS, Pair(true, favorite), null))
        } catch (e: Exception) {
            favoriteSavedState.postValue(Resource(Status.ERROR, null, e))
        }
    }

    fun getMovieDetail(): LiveData<Resource<MovieDetail>> = movieDetail

    fun getProgressState(): LiveData<Boolean> = progressState

    fun getFavoriteSavedState(): SingleLiveEvent<Resource<Pair<Boolean, Boolean>>> = favoriteSavedState

    fun movieIsFavorite(id: String): LiveData<List<MovieResult>> = repository.existAsFavorite(id)
}