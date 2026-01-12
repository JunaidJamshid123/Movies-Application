package com.example.movieapp.domain.usecase

import com.example.movieapp.domain.model.Video
import com.example.movieapp.domain.repository.MovieRepository
import javax.inject.Inject

class GetMovieVideosUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int): List<Video> {
        return repository.getMovieVideos(movieId)
    }
}
