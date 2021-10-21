package com.scientisthamsterssofiandjohn.samoyedtv.di.module

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.scientisthamsterssofiandjohn.samoyedtv.App
import com.scientisthamsterssofiandjohn.samoyedtv.data.local.MovieDao
import com.scientisthamsterssofiandjohn.samoyedtv.data.local.MovieLocalStorage
import com.scientisthamsterssofiandjohn.samoyedtv.data.remote.MovieApi
import com.scientisthamsterssofiandjohn.samoyedtv.domain.repository.MovieRepository
import com.scientisthamsterssofiandjohn.samoyedtv.util.Constants.Companion.DATABASE
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

    @Provides
    internal fun provideContext(application: App): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideMovieApi(retrofit: Retrofit): MovieApi {
        return retrofit.create(MovieApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieRepository(movieApi: MovieApi, movieDao: MovieDao): MovieRepository {
        return MovieRepository(movieApi, movieDao)
    }

    @Provides
    @Singleton
    fun provideDatabase(context: Context): MovieLocalStorage = Room.databaseBuilder(
        context,
        MovieLocalStorage::class.java,
        DATABASE
    )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideMovieDao(database: MovieLocalStorage): MovieDao = database.movieDao()
}