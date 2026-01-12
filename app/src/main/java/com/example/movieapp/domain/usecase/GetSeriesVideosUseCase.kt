package com.example.movieapp.domain.usecase

import com.example.movieapp.domain.model.Video
import com.example.movieapp.domain.repository.MovieRepository
import javax.inject.Inject

class GetSeriesVideosUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(seriesId: Int): List<Video> {
        return repository.getSeriesVideos(seriesId)
    }
}
