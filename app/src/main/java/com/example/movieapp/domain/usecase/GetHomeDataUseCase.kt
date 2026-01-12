package com.example.movieapp.domain.usecase

import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.Series
import com.example.movieapp.domain.repository.MovieRepository
import javax.inject.Inject

data class HomeData(
    val trendingMovies: List<Movie>,
    val popularMovies: List<Movie>,
    val trendingSeries: List<Series>,
    val popularSeries: List<Series>
)

class GetHomeDataUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(): HomeData {
        return HomeData(
            trendingMovies = repository.getTrendingMovies(),
            popularMovies = repository.getPopularMovies(),
            trendingSeries = repository.getTrendingSeries(),
            popularSeries = repository.getPopularSeries()
        )
    }
}
