package com.example.movieapp.domain.usecase

import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.Series
import com.example.movieapp.domain.repository.MovieRepository
import javax.inject.Inject

class AddOrRemoveFavoriteUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend fun toggleMovie(movie: Movie) {
        repository.toggleMovieFavorite(movie)
    }

    suspend fun toggleSeries(series: Series) {
        repository.toggleSeriesFavorite(series)
    }
}