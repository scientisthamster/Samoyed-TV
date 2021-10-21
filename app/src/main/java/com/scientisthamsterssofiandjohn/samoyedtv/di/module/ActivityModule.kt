package com.scientisthamsterssofiandjohn.samoyedtv.di.module

import com.scientisthamsterssofiandjohn.samoyedtv.view.ui.MovieActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityModule {
    @ContributesAndroidInjector(modules = [FragmentModule::class])
    fun contributeMovieActivity(): MovieActivity
}