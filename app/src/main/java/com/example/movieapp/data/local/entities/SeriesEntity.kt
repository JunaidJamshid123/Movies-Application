package com.example.movieapp.data.local.entities
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "series")
data class SeriesEntity(
    @PrimaryKey
    val id: Int,
    val adult: Boolean,
    val backdropPath: String?,
    val genreIds: String, // Comma-separated: "18,80"
    val originCountry: String, // Comma-separated: "US,GB"
    val originalLanguage: String,
    val originalName: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String?,
    val firstAirDate: String?,
    val name: String,
    val voteAverage: Double,
    val voteCount: Int,
    val isFavorite: Boolean = false,
    val addedAt: Long = System.currentTimeMillis()
)