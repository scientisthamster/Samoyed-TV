package com.scientisthamsterssofiandjohn.samoyedtv

import androidx.appcompat.app.AppCompatDelegate
import com.scientisthamsterssofiandjohn.samoyedtv.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class App : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.builder().application(this)!!.build()


    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }
}