package com.example.movieapp.domain.model

// TODO: Implement Movie model
data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String,
    val backdropUrl: String,
    val releaseDate: String,
    val voteAverage: Double,
    val genreIds: List<Int> = emptyList()
)

data class Genre(
    val id: Int,
    val name: String
)

data class ProductionCompany(
    val id: Int,
    val name: String,
    val logoPath: String?,
    val originCountry: String
)

data class MovieDetails(
    val id: Int,
    val title: String,
    val originalTitle: String,
    val overview: String,
    val posterUrl: String,
    val backdropUrl: String,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int,
    val runtime: Int,
    val genres: List<Genre>,
    val budget: Long,
    val revenue: Long,
    val status: String,
    val tagline: String,
    val homepage: String?,
    val imdbId: String?,
    val productionCompanies: List<ProductionCompany>,
    val popularity: Double
)