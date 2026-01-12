package com.example.movieapp.domain.usecase

import com.example.movieapp.domain.model.Series
import com.example.movieapp.domain.repository.MovieRepository
import javax.inject.Inject

class GetSimilarSeriesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(seriesId: Int): List<Series> {
        return repository.getSimilarSeries(seriesId)
    }
}
