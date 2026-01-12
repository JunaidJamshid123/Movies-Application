package com.example.movieapp.data.local.mapper

import com.example.movieapp.data.local.entities.SeriesEntity
import com.example.movieapp.data.local.entities.SeriesDetailsEntity
import com.example.movieapp.data.remote.dto.SeriesDto
import com.example.movieapp.data.remote.dto.SeriesDetailsDto
import com.example.movieapp.domain.model.Series
import com.example.movieapp.domain.model.SeriesDetails
import com.example.movieapp.domain.model.Genre
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// ========== DTO TO ENTITY ==========

fun SeriesDto.toEntity(): SeriesEntity {
    return SeriesEntity(
        id = this.id,
        adult = this.adult,
        backdropPath = this.backdrop_path,
        genreIds = this.genre_ids.joinToString(","),
        originCountry = this.origin_country.joinToString(","),
        originalLanguage = this.original_language,
        originalName = this.original_name,
        overview = this.overview,
        popularity = this.popularity,
        posterPath = this.poster_path,
        firstAirDate = this.first_air_date,
        name = this.name,
        voteAverage = this.vote_average,
        voteCount = this.vote_count
    )
}

fun SeriesDetailsDto.toEntity(): SeriesDetailsEntity {
    val gson = Gson()
    return SeriesDetailsEntity(
        id = this.id,
        adult = this.adult,
        backdropPath = this.backdrop_path,
        createdBy = gson.toJson(this.created_by),
        episodeRunTime = this.episode_run_time.joinToString(","),
        firstAirDate = this.first_air_date,
        genres = gson.toJson(this.genres),
        homepage = this.homepage,
        inProduction = this.in_production,
        languages = this.languages.joinToString(","),
        lastAirDate = this.last_air_date,
        name = this.name,
        numberOfEpisodes = this.number_of_episodes,
        numberOfSeasons = this.number_of_seasons,
        originCountry = this.origin_country.joinToString(","),
        originalLanguage = this.original_language,
        originalName = this.original_name,
        overview = this.overview,
        popularity = this.popularity,
        posterPath = this.poster_path,
        productionCompanies = gson.toJson(this.production_companies),
        status = this.status,
        tagline = this.tagline,
        type = this.type,
        voteAverage = this.vote_average,
        voteCount = this.vote_count
    )
}

// ========== ENTITY TO DOMAIN ==========

fun SeriesEntity.toDomain(baseImageUrl: String = "https://image.tmdb.org/t/p/w500"): Series {
    return Series(
        id = this.id,
        name = this.name,
        overview = this.overview,
        posterUrl = posterPath?.let { baseImageUrl + it } ?: "",
        backdropUrl = backdropPath?.let { baseImageUrl + it } ?: "",
        firstAirDate = this.firstAirDate ?: "",
        voteAverage = this.voteAverage
    )
}

fun SeriesDetailsEntity.toDomain(baseImageUrl: String = "https://image.tmdb.org/t/p/w500"): SeriesDetails {
    val gson = Gson()
    val genreType = object : TypeToken<List<Map<String, Any>>>() {}.type
    val genreList: List<Map<String, Any>> = gson.fromJson(genres, genreType)

    return SeriesDetails(
        id = this.id,
        name = this.name,
        originalName = this.originalName,
        overview = this.overview,
        posterUrl = posterPath?.let { baseImageUrl + it } ?: "",
        backdropUrl = backdropPath?.let { baseImageUrl + it } ?: "",
        firstAirDate = this.firstAirDate ?: "",
        voteAverage = this.voteAverage,
        voteCount = this.voteCount,
        numberOfSeasons = this.numberOfSeasons,
        numberOfEpisodes = this.numberOfEpisodes,
        genres = genreList.map { Genre(
            id = (it["id"] as Double).toInt(),
            name = it["name"] as String
        ) },
        status = this.status,
        tagline = this.tagline,
        homepage = this.homepage,
        popularity = this.popularity
    )
}

// ========== DOMAIN TO ENTITY ==========

fun Series.toEntity(): SeriesEntity {
    return SeriesEntity(
        id = this.id,
        adult = false,
        backdropPath = this.backdropUrl.substringAfter("w500"),
        genreIds = "",
        originCountry = "",
        originalLanguage = "",
        originalName = this.name,
        overview = this.overview,
        popularity = 0.0,
        posterPath = this.posterUrl.substringAfter("w500"),
        firstAirDate = this.firstAirDate,
        name = this.name,
        voteAverage = this.voteAverage,
        voteCount = 0
    )
}