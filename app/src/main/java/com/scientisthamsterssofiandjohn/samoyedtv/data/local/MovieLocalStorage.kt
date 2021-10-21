package com.scientisthamsterssofiandjohn.samoyedtv.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.scientisthamsterssofiandjohn.samoyedtv.domain.model.MovieResult

@Database(entities = [MovieResult::class], version = 1, exportSchema = false)
abstract class MovieLocalStorage : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}