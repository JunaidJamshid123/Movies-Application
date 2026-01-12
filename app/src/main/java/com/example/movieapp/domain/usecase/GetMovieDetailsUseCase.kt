package com.example.movieapp.domain.usecase

import com.example.movieapp.domain.model.MovieDetails
import com.example.movieapp.domain.repository.MovieRepository
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int): MovieDetails {
        return repository.getMovieDetails(movieId)
    }
}
