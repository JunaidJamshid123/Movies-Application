package com.example.movieapp.domain.usecase

import com.example.movieapp.domain.model.SeriesDetails
import com.example.movieapp.domain.repository.MovieRepository
import javax.inject.Inject

class GetSeriesDetailsUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(seriesId: Int): SeriesDetails {
        return repository.getSeriesDetails(seriesId)
    }
}
