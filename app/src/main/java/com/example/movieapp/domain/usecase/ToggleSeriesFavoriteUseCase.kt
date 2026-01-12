package com.example.movieapp.domain.usecase

import com.example.movieapp.domain.model.Series
import com.example.movieapp.domain.repository.MovieRepository
import javax.inject.Inject

class ToggleSeriesFavoriteUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(series: Series) {
        repository.toggleSeriesFavorite(series)
    }
}
