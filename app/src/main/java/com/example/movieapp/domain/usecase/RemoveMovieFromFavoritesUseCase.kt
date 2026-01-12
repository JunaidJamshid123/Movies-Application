package com.example.movieapp.domain.usecase

import com.example.movieapp.domain.repository.MovieRepository
import javax.inject.Inject

class RemoveMovieFromFavoritesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int) {
        repository.removeMovieFromFavorites(movieId)
    }
}
