package com.example.movieapp.data.local.entities
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_details")
data class MovieDetailsEntity(
    @PrimaryKey
    val id: Int,
    val adult: Boolean,
    val backdropPath: String?,
    val budget: Long,
    val genres: String, // JSON string: "[{\"id\":28,\"name\":\"Action\"}]"
    val homepage: String?,
    val imdbId: String?,
    val originCountry: String, // Comma-separated
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String?,
    val productionCompanies: String, // JSON string
    val releaseDate: String?,
    val revenue: Long,
    val runtime: Int,
    val status: String,
    val tagline: String,
    val title: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int,
    val isFavorite: Boolean = false,
    val cachedAt: Long = System.currentTimeMillis()
)