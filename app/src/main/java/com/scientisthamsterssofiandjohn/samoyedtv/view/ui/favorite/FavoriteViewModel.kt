package com.scientisthamsterssofiandjohn.samoyedtv.view.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.scientisthamsterssofiandjohn.samoyedtv.data.Resource
import com.scientisthamsterssofiandjohn.samoyedtv.data.Status
import com.scientisthamsterssofiandjohn.samoyedtv.domain.model.MovieResult
import com.scientisthamsterssofiandjohn.samoyedtv.domain.repository.MovieRepository
import com.scientisthamsterssofiandjohn.samoyedtv.view.base.BaseViewModel
import javax.inject.Inject

class FavoriteViewModel @Inject constructor(private val repository: MovieRepository) :
    BaseViewModel() {

    fun getMovieFavorites(): LiveData<Resource<List<MovieResult>>> {
        progressState.postValue(true)
        return Transformations.map(repository.allFavoriteMovie()) {
            progressState.postValue(false)
            Resource(Status.SUCCESS, it, null)
        }
    }

    fun getProgressState(): LiveData<Boolean> = progressState
}