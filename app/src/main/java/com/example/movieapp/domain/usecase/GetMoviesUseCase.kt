package com.example.movieapp.domain.usecase

import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.repository.MovieRepository
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend fun getPopular(): List<Movie> {
        return repository.getPopularMovies()
    }

    suspend fun getTopRated(): List<Movie> {
        return repository.getTopRatedMovies()
    }

}
