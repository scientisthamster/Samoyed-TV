package com.scientisthamsterssofiandjohn.samoyedtv.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.scientisthamsterssofiandjohn.samoyedtv.common.ViewModelFactory
import com.scientisthamsterssofiandjohn.samoyedtv.di.key.ViewModelKey
import com.scientisthamsterssofiandjohn.samoyedtv.view.ui.detail.DetailViewModel
import com.scientisthamsterssofiandjohn.samoyedtv.view.ui.favorite.FavoriteViewModel
import com.scientisthamsterssofiandjohn.samoyedtv.view.ui.find.FindViewModel
import com.scientisthamsterssofiandjohn.samoyedtv.view.ui.home.HomeViewModel
import com.scientisthamsterssofiandjohn.samoyedtv.view.ui.popular.PopularViewModel
import com.scientisthamsterssofiandjohn.samoyedtv.view.ui.top_rate.TopRateViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    fun bindHomeViewModel(homeViewModel: HomeViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteViewModel::class)
    fun bindFavoriteViewModel(favoriteViewModel: FavoriteViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FindViewModel::class)
    fun bindFindViewModel(findViewModel: FindViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailViewModel::class)
    fun bindDetailViewModel(detailViewModel: DetailViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PopularViewModel::class)
    fun bindPopularViewModel(popularViewModel: PopularViewModel) : ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TopRateViewModel::class)
    fun bindChildViewModel(topRateViewModel: TopRateViewModel) : ViewModel

    @Binds
    fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}