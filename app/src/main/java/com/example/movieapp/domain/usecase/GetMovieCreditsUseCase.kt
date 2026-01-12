package com.example.movieapp.domain.usecase

import com.example.movieapp.domain.model.Credits
import com.example.movieapp.domain.repository.MovieRepository
import javax.inject.Inject

class GetMovieCreditsUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int): Credits {
        return repository.getMovieCredits(movieId)
    }
}
