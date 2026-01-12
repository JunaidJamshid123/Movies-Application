package com.example.movieapp.data.local.mapper

import com.example.movieapp.data.local.entities.MovieEntity
import com.example.movieapp.data.local.entities.MovieDetailsEntity
import com.example.movieapp.data.remote.dto.MovieDto
import com.example.movieapp.data.remote.dto.MovieDetailsDto
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.MovieDetails
import com.example.movieapp.domain.model.Genre
import com.example.movieapp.domain.model.ProductionCompany
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// ========== DTO TO ENTITY ==========

fun MovieDto.toEntity(): MovieEntity {
    return MovieEntity(
        id = this.id,
        adult = this.adult,
        backdropPath = this.backdrop_path,
        genreIds = this.genre_ids.joinToString(","),
        originalLanguage = this.original_language,
        originalTitle = this.original_title,
        overview = this.overview,
        popularity = this.popularity,
        posterPath = this.poster_path,
        releaseDate = this.release_date,
        title = this.title,
        video = this.video,
        voteAverage = this.vote_average,
        voteCount = this.vote_count
    )
}

fun MovieDetailsDto.toEntity(): MovieDetailsEntity {
    val gson = Gson()
    return MovieDetailsEntity(
        id = this.id,
        adult = this.adult,
        backdropPath = this.backdrop_path,
        budget = this.budget,
        genres = gson.toJson(this.genres),
        homepage = this.homepage,
        imdbId = this.imdb_id,
        originCountry = this.origin_country.joinToString(","),
        originalLanguage = this.original_language,
        originalTitle = this.original_title,
        overview = this.overview,
        popularity = this.popularity,
        posterPath = this.poster_path,
        productionCompanies = gson.toJson(this.production_companies),
        releaseDate = this.release_date,
        revenue = this.revenue,
        runtime = this.runtime,
        status = this.status,
        tagline = this.tagline,
        title = this.title,
        video = this.video,
        voteAverage = this.vote_average,
        voteCount = this.vote_count
    )
}

// ========== ENTITY TO DOMAIN ==========

fun MovieEntity.toDomain(baseImageUrl: String = "https://image.tmdb.org/t/p/w500"): Movie {
    return Movie(
        id = this.id,
        title = this.title,
        overview = this.overview,
        posterUrl = posterPath?.let { baseImageUrl + it } ?: "",
        backdropUrl = backdropPath?.let { baseImageUrl + it } ?: "",
        releaseDate = this.releaseDate ?: "",
        voteAverage = this.voteAverage,
        genreIds = this.genreIds.split(",").mapNotNull { it.toIntOrNull() }
    )
}

fun MovieDetailsEntity.toDomain(baseImageUrl: String = "https://image.tmdb.org/t/p/w500"): MovieDetails {
    val gson = Gson()
    val genreType = object : TypeToken<List<Map<String, Any>>>() {}.type
    val companyType = object : TypeToken<List<Map<String, Any>>>() {}.type

    val genreList: List<Map<String, Any>> = gson.fromJson(genres, genreType)
    val companyList: List<Map<String, Any>> = gson.fromJson(productionCompanies, companyType)

    return MovieDetails(
        id = this.id,
        title = this.title,
        originalTitle = this.originalTitle,
        overview = this.overview,
        posterUrl = posterPath?.let { baseImageUrl + it } ?: "",
        backdropUrl = backdropPath?.let { baseImageUrl + it } ?: "",
        releaseDate = this.releaseDate ?: "",
        voteAverage = this.voteAverage,
        voteCount = this.voteCount,
        runtime = this.runtime,
        genres = genreList.map { Genre(
            id = (it["id"] as Double).toInt(),
            name = it["name"] as String
        ) },
        budget = this.budget,
        revenue = this.revenue,
        status = this.status,
        tagline = this.tagline,
        homepage = this.homepage,
        imdbId = this.imdbId,
        productionCompanies = companyList.map { company ->
            ProductionCompany(
                id = (company["id"] as Double).toInt(),
                name = company["name"] as String,
                logoPath = company["logo_path"] as? String,
                originCountry = company["origin_country"] as String
            )
        },
        popularity = this.popularity
    )
}

// ========== DOMAIN TO ENTITY ==========

fun Movie.toEntity(): MovieEntity {
    return MovieEntity(
        id = this.id,
        adult = false,
        backdropPath = this.backdropUrl.substringAfter("w500"),
        genreIds = this.genreIds.joinToString(","),
        originalLanguage = "",
        originalTitle = this.title,
        overview = this.overview,
        popularity = 0.0,
        posterPath = this.posterUrl.substringAfter("w500"),
        releaseDate = this.releaseDate,
        title = this.title,
        video = false,
        voteAverage = this.voteAverage,
        voteCount = 0
    )
}