package com.example.movieapp.domain.usecase

import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.Series
import com.example.movieapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    fun getFavoriteMovies(): Flow<List<Movie>> {
        return repository.getFavoriteMovies()
    }

    fun getFavoriteSeries(): Flow<List<Series>> {
        return repository.getFavoriteSeries()
    }
}