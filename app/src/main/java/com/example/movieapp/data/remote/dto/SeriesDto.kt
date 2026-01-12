package com.example.movieapp.data.remote.dto

// TODO: Implement SeriesDto
data class SeriesDto(
    val adult: Boolean,
    val backdrop_path: String?,
    val genre_ids: List<Int>,
    val id: Int,
    val origin_country: List<String>,
    val original_language: String,
    val original_name: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String?,
    val first_air_date: String?,
    val name: String,
    val vote_average: Double,
    val vote_count: Int
)

data class SeriesResponseDto(
    val page: Int,
    val results: List<SeriesDto>
)
