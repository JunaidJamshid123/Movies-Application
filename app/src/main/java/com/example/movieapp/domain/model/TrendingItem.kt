package com.example.movieapp.domain.model

data class TrendingItem(
    val id: Int,
    val title: String,
    val name: String = title, // For series
    val overview: String,
    val posterUrl: String,
    val backdropUrl: String,
    val releaseDate: String,
    val voteAverage: Double,
    val mediaType: String // "movie" or "tv"
)
