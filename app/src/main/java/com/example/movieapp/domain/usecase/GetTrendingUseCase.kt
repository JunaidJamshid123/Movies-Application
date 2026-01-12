package com.example.movieapp.domain.usecase

import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.Series
import com.example.movieapp.domain.repository.MovieRepository
import javax.inject.Inject

data class TrendingData(
    val movies: List<Movie>,
    val series: List<Series>
)

class GetTrendingUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(): TrendingData {
        return TrendingData(
            movies = repository.getTrendingMovies(),
            series = repository.getTrendingSeries()
        )
    }
}
