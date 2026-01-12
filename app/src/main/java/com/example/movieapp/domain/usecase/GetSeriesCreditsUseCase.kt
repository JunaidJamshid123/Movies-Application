package com.example.movieapp.domain.usecase

import com.example.movieapp.domain.model.Credits
import com.example.movieapp.domain.repository.MovieRepository
import javax.inject.Inject

class GetSeriesCreditsUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(seriesId: Int): Credits {
        return repository.getSeriesCredits(seriesId)
    }
}
