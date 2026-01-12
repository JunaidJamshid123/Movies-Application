package com.example.movieapp.domain.model

// TODO: Implement Series model
data class Series(
    val id: Int,
    val name: String,
    val overview: String,
    val posterUrl: String,
    val backdropUrl: String,
    val firstAirDate: String,
    val voteAverage: Double
)

data class SeriesDetails(
    val id: Int,
    val name: String,
    val originalName: String,
    val overview: String,
    val posterUrl: String,
    val backdropUrl: String,
    val firstAirDate: String,
    val voteAverage: Double,
    val voteCount: Int,
    val numberOfSeasons: Int,
    val numberOfEpisodes: Int,
    val genres: List<Genre>,
    val status: String,
    val tagline: String,
    val homepage: String?,
    val popularity: Double
)