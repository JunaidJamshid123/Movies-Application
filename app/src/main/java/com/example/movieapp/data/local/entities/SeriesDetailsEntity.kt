package com.example.movieapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "series_details")
data class SeriesDetailsEntity(
    @PrimaryKey
    val id: Int,
    val adult: Boolean,
    val backdropPath: String?,
    val createdBy: String, // JSON string of creators
    val episodeRunTime: String, // Comma-separated: "45,60"
    val firstAirDate: String?,
    val genres: String, // JSON string
    val homepage: String?,
    val inProduction: Boolean,
    val languages: String, // Comma-separated
    val lastAirDate: String?,
    val name: String,
    val numberOfEpisodes: Int,
    val numberOfSeasons: Int,
    val originCountry: String, // Comma-separated
    val originalLanguage: String,
    val originalName: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String?,
    val productionCompanies: String, // JSON string
    val status: String,
    val tagline: String,
    val type: String,
    val voteAverage: Double,
    val voteCount: Int,
    val isFavorite: Boolean = false,
    val cachedAt: Long = System.currentTimeMillis()
)