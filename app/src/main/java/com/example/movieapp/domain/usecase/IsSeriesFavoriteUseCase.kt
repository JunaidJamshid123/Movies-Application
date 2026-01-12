package com.example.movieapp.domain.usecase

import com.example.movieapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsSeriesFavoriteUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(seriesId: Int): Flow<Boolean> {
        return repository.isSeriesFavorite(seriesId)
    }
}
