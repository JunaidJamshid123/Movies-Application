package com.example.movieapp.data.remote.dto

data class SeriesDetailsDto(
    val adult: Boolean,
    val backdrop_path: String?,
    val created_by: List<CreatorDto>,
    val episode_run_time: List<Int>,
    val first_air_date: String?,
    val genres: List<GenreDto>,
    val homepage: String?,
    val id: Int,
    val in_production: Boolean,
    val languages: List<String>,
    val last_air_date: String?,
    val name: String,
    val number_of_episodes: Int,
    val number_of_seasons: Int,
    val origin_country: List<String>,
    val original_language: String,
    val original_name: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String?,
    val production_companies: List<ProductionCompanyDto>,
    val status: String,
    val tagline: String,
    val type: String,
    val vote_average: Double,
    val vote_count: Int
)

data class CreatorDto(
    val id: Int,
    val name: String,
    val profile_path: String?
)
