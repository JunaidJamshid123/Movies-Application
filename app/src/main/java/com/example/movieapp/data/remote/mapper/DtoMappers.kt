package com.example.movieapp.data.remote.mapper

// TODO: Implement DTO mappers

import com.example.movieapp.data.remote.dto.*
import com.example.movieapp.domain.model.*

// ----------------- MOVIE -----------------
fun MovieDto.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        posterUrl = poster_path?.let { "https://image.tmdb.org/t/p/w500$it" } ?: "",
        backdropUrl = backdrop_path?.let { "https://image.tmdb.org/t/p/w500$it" } ?: "",
        releaseDate = release_date ?: "",
        voteAverage = vote_average,
        genreIds = genre_ids
    )
}

fun MovieDetailsDto.toDomain(): MovieDetails {
    return MovieDetails(
        id = id,
        title = title,
        originalTitle = original_title,
        overview = overview,
        posterUrl = poster_path?.let { "https://image.tmdb.org/t/p/w500$it" } ?: "",
        backdropUrl = backdrop_path?.let { "https://image.tmdb.org/t/p/w780$it" } ?: "",
        releaseDate = release_date ?: "",
        voteAverage = vote_average,
        voteCount = vote_count,
        runtime = runtime,
        genres = genres.map { it.toDomain() },
        budget = budget,
        revenue = revenue,
        status = status,
        tagline = tagline,
        homepage = homepage,
        imdbId = imdb_id,
        productionCompanies = production_companies.map { it.toDomain() },
        popularity = popularity
    )
}

fun GenreDto.toDomain(): Genre {
    return Genre(
        id = id,
        name = name
    )
}

fun ProductionCompanyDto.toDomain(): ProductionCompany {
    return ProductionCompany(
        id = id,
        name = name,
        logoPath = logo_path?.let { "https://image.tmdb.org/t/p/w500$it" },
        originCountry = origin_country
    )
}

// ----------------- VIDEO -----------------
fun VideoDto.toDomain(): Video {
    return Video(
        id = id,
        key = key,
        name = name,
        site = site,
        type = type,
        official = official
    )
}

// ----------------- SERIES -----------------
fun SeriesDto.toDomain(): Series {
    return Series(
        id = id,
        name = name,
        overview = overview,
        posterUrl = poster_path?.let { "https://image.tmdb.org/t/p/w500$it" } ?: "",
        backdropUrl = backdrop_path?.let { "https://image.tmdb.org/t/p/w500$it" } ?: "",
        firstAirDate = first_air_date ?: "",
        voteAverage = vote_average
    )
}

fun SeriesDetailsDto.toDomain(): SeriesDetails {
    return SeriesDetails(
        id = id,
        name = name,
        originalName = original_name,
        overview = overview,
        posterUrl = poster_path?.let { "https://image.tmdb.org/t/p/w500$it" } ?: "",
        backdropUrl = backdrop_path?.let { "https://image.tmdb.org/t/p/w780$it" } ?: "",
        firstAirDate = first_air_date ?: "",
        voteAverage = vote_average,
        voteCount = vote_count,
        numberOfSeasons = number_of_seasons,
        numberOfEpisodes = number_of_episodes,
        genres = genres.map { it.toDomain() },
        status = status,
        tagline = tagline,
        homepage = homepage,
        popularity = popularity
    )
}

// ----------------- CREDITS -----------------
fun CastDto.toDomain(): Cast {
    return Cast(
        id = id,
        name = name,
        character = character ?: "",
        profileUrl = profilePath?.let { "https://image.tmdb.org/t/p/w185$it" },
        order = order,
        department = knownForDepartment ?: "Acting"
    )
}

fun CrewDto.toDomain(): Crew {
    return Crew(
        id = id,
        name = name,
        job = job,
        department = department,
        profileUrl = profilePath?.let { "https://image.tmdb.org/t/p/w185$it" }
    )
}

fun CreditsResponseDto.toDomain(): Credits {
    return Credits(
        cast = cast.map { it.toDomain() },
        crew = crew.map { it.toDomain() }
    )
}

fun PersonDetailsDto.toDomain(): PersonDetails {
    return PersonDetails(
        id = id,
        name = name,
        biography = biography ?: "",
        birthday = birthday,
        deathday = deathday,
        placeOfBirth = placeOfBirth,
        profileUrl = profilePath?.let { "https://image.tmdb.org/t/p/w500$it" },
        knownFor = knownForDepartment ?: "Acting",
        popularity = popularity
    )
}

fun PersonCastCreditDto.toDomain(): PersonCredit {
    return PersonCredit(
        id = id,
        title = title ?: name ?: "",
        character = character,
        job = null,
        posterUrl = posterPath?.let { "https://image.tmdb.org/t/p/w342$it" },
        releaseDate = releaseDate ?: firstAirDate ?: "",
        voteAverage = voteAverage,
        mediaType = mediaType ?: "movie"
    )
}

fun PersonCrewCreditDto.toDomain(): PersonCredit {
    return PersonCredit(
        id = id,
        title = title ?: name ?: "",
        character = null,
        job = job,
        posterUrl = posterPath?.let { "https://image.tmdb.org/t/p/w342$it" },
        releaseDate = releaseDate ?: firstAirDate ?: "",
        voteAverage = voteAverage,
        mediaType = mediaType ?: "movie"
    )
}

fun PersonCreditsDto.toDomain(): PersonCredits {
    return PersonCredits(
        cast = cast.map { it.toDomain() },
        crew = crew.map { it.toDomain() }
    )
}
