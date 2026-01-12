package com.example.movieapp.domain.usecase

import com.example.movieapp.domain.model.Series
import com.example.movieapp.domain.repository.MovieRepository
import javax.inject.Inject

class GetSeriesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend fun getPopular(): List<Series> {
        return repository.getPopularSeries()
    }
    
    suspend fun getTopRated(): List<Series> {
        return repository.getTopRatedSeries()
    }
}


