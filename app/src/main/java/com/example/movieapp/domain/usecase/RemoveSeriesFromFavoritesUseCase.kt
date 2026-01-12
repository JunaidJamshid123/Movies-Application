package com.example.movieapp.domain.usecase

import com.example.movieapp.domain.repository.MovieRepository
import javax.inject.Inject

class RemoveSeriesFromFavoritesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(seriesId: Int) {
        repository.removeSeriesFromFavorites(seriesId)
    }
}
