package com.example.movieapp.data.remote.dto

data class GenreDto(
    val id: Int,
    val name: String
)

data class ProductionCompanyDto(
    val id: Int,
    val name: String,
    val logo_path: String?,
    val origin_country: String
)

data class MovieDetailsDto(
    val adult: Boolean,
    val backdrop_path: String?,
    val belongs_to_collection: Any?,
    val budget: Long,
    val genres: List<GenreDto>,
    val homepage: String?,
    val id: Int,
    val imdb_id: String?,
    val origin_country: List<String>,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String?,
    val production_companies: List<ProductionCompanyDto>,
    val production_countries: List<ProductionCountryDto>,
    val release_date: String?,
    val revenue: Long,
    val runtime: Int,
    val spoken_languages: List<SpokenLanguageDto>,
    val status: String,
    val tagline: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
)

data class ProductionCountryDto(
    val iso_3166_1: String,
    val name: String
)

data class SpokenLanguageDto(
    val english_name: String,
    val iso_639_1: String,
    val name: String
)
