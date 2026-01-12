package com.example.movieapp.data.remote.dto

data class VideoDto(
    val id: String,
    val iso_639_1: String,
    val iso_3166_1: String,
    val key: String,
    val name: String,
    val site: String,
    val size: Int,
    val type: String,
    val official: Boolean,
    val published_at: String
)

data class VideoResponseDto(
    val id: Int,
    val results: List<VideoDto>
)
