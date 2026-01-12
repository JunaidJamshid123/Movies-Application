package com.example.movieapp.domain.usecase

import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.repository.MovieRepository
import javax.inject.Inject

class AddMovieToFavoritesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movie: Movie) {
        repository.addMovieToFavorites(movie)
    }
}
