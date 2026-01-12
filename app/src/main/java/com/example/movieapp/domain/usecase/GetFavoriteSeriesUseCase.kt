package com.example.movieapp.domain.usecase

import com.example.movieapp.domain.model.Series
import com.example.movieapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteSeriesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(): Flow<List<Series>> {
        return repository.getFavoriteSeries()
    }
}
