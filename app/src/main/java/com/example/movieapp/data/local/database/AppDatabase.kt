package com.example.movieapp.data.local.database
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.movieapp.data.local.dao.MovieDao
import com.example.movieapp.data.local.dao.SeriesDao
import com.example.movieapp.data.local.entities.MovieEntity
import com.example.movieapp.data.local.entities.MovieDetailsEntity
import com.example.movieapp.data.local.entities.SeriesEntity
import com.example.movieapp.data.local.entities.SeriesDetailsEntity

@Database(
    entities = [
        MovieEntity::class,
        MovieDetailsEntity::class,
        SeriesEntity::class,
        SeriesDetailsEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun seriesDao(): SeriesDao

    companion object {
        const val DATABASE_NAME = "movie_app_database"
    }
}