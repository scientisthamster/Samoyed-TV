package com.scientisthamsterssofiandjohn.samoyedtv.view.ui.home.adapter

import com.scientisthamsterssofiandjohn.samoyedtv.domain.model.MovieResult

interface movieInteractionListener {
    fun onClickRetry()
    fun onMovieClick(movieResult: MovieResult, position: Int)
}