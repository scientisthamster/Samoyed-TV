package com.scientisthamsterssofiandjohn.samoyedtv.di.module

import com.scientisthamsterssofiandjohn.samoyedtv.view.ui.account.AccountFragment
import com.scientisthamsterssofiandjohn.samoyedtv.view.ui.detail.DetailFragment
import com.scientisthamsterssofiandjohn.samoyedtv.view.ui.favorite.FavoriteFragment
import com.scientisthamsterssofiandjohn.samoyedtv.view.ui.find.FindFragment
import com.scientisthamsterssofiandjohn.samoyedtv.view.ui.home.HomeFragment
import com.scientisthamsterssofiandjohn.samoyedtv.view.ui.popular.PopularFragment
import com.scientisthamsterssofiandjohn.samoyedtv.view.ui.top_rate.TopRateFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FragmentModule {

    @ContributesAndroidInjector
    fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    fun contributeFindFragment(): FindFragment

    @ContributesAndroidInjector
    fun contributeDetailFragment(): DetailFragment

    @ContributesAndroidInjector
    fun contributeFavoriteFragment(): FavoriteFragment

    @ContributesAndroidInjector
    fun contributePopularFragment(): PopularFragment

    @ContributesAndroidInjector
    fun contributeTopRateFragment(): TopRateFragment

    @ContributesAndroidInjector
    fun contributeAccountFragment(): AccountFragment
}