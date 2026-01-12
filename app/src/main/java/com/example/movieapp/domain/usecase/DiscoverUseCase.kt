package com.example.movieapp.domain.usecase

import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.Series
import com.example.movieapp.domain.repository.MovieRepository
import javax.inject.Inject

class DiscoverMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(
        genres: String? = null,
        releaseDateGte: String? = null,
        releaseDateLte: String? = null,
        voteAverageGte: Float? = null,
        voteAverageLte: Float? = null,
        runtimeGte: Int? = null,
        runtimeLte: Int? = null,
        language: String? = null,
        sortBy: String = "popularity.desc"
    ): List<Movie> {
        return repository.discoverMovies(
            genres = genres,
            releaseDateGte = releaseDateGte,
            releaseDateLte = releaseDateLte,
            voteAverageGte = voteAverageGte,
            voteAverageLte = voteAverageLte,
            runtimeGte = runtimeGte,
            runtimeLte = runtimeLte,
            language = language,
            sortBy = sortBy
        )
    }
}

class DiscoverSeriesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(
        genres: String? = null,
        firstAirDateGte: String? = null,
        firstAirDateLte: String? = null,
        voteAverageGte: Float? = null,
        voteAverageLte: Float? = null,
        language: String? = null,
        sortBy: String = "popularity.desc"
    ): List<Series> {
        return repository.discoverSeries(
            genres = genres,
            firstAirDateGte = firstAirDateGte,
            firstAirDateLte = firstAirDateLte,
            voteAverageGte = voteAverageGte,
            voteAverageLte = voteAverageLte,
            language = language,
            sortBy = sortBy
        )
    }
}
