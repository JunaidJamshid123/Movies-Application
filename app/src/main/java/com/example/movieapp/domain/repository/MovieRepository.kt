package com.example.movieapp.domain.repository

import com.example.movieapp.domain.model.Credits
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.MovieDetails
import com.example.movieapp.domain.model.PersonCredits
import com.example.movieapp.domain.model.PersonDetails
import com.example.movieapp.domain.model.Series
import com.example.movieapp.domain.model.SeriesDetails
import com.example.movieapp.domain.model.TrendingItem
import com.example.movieapp.domain.model.Video
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getPopularMovies(): List<Movie>
    suspend fun getTopRatedMovies(): List<Movie>
    suspend fun getPopularSeries(): List<Series>
    suspend fun getTopRatedSeries(): List<Series>
    suspend fun getTrendingMovies(): List<Movie>
    suspend fun getTrendingSeries(): List<Series>
    suspend fun getMovieDetails(movieId: Int): MovieDetails
    suspend fun getSeriesDetails(seriesId: Int): SeriesDetails
    suspend fun getMovieVideos(movieId: Int): List<Video>
    suspend fun getSeriesVideos(seriesId: Int): List<Video>

    // Credits (Cast & Crew)
    suspend fun getMovieCredits(movieId: Int): Credits
    suspend fun getSeriesCredits(seriesId: Int): Credits

    // Similar & Recommendations
    suspend fun getSimilarMovies(movieId: Int): List<Movie>
    suspend fun getMovieRecommendations(movieId: Int): List<Movie>
    suspend fun getSimilarSeries(seriesId: Int): List<Series>
    suspend fun getSeriesRecommendations(seriesId: Int): List<Series>

    // Person Details
    suspend fun getPersonDetails(personId: Int): PersonDetails
    suspend fun getPersonCredits(personId: Int): PersonCredits

    // Discover (for filters and mood)
    suspend fun discoverMovies(
        genres: String? = null,
        releaseDateGte: String? = null,
        releaseDateLte: String? = null,
        voteAverageGte: Float? = null,
        voteAverageLte: Float? = null,
        runtimeGte: Int? = null,
        runtimeLte: Int? = null,
        language: String? = null,
        sortBy: String = "popularity.desc"
    ): List<Movie>

    suspend fun discoverSeries(
        genres: String? = null,
        firstAirDateGte: String? = null,
        firstAirDateLte: String? = null,
        voteAverageGte: Float? = null,
        voteAverageLte: Float? = null,
        language: String? = null,
        sortBy: String = "popularity.desc"
    ): List<Series>

    // Search
    suspend fun searchMovies(query: String): List<Movie>
    suspend fun searchSeries(query: String): List<Series>

    // Local Database - Movies
    suspend fun addMovieToFavorites(movie: Movie)
    suspend fun removeMovieFromFavorites(movieId: Int)
    fun getFavoriteMovies(): Flow<List<Movie>>
    fun isMovieFavorite(movieId: Int): Flow<Boolean>
    suspend fun toggleMovieFavorite(movie: Movie)


    // Local Database - Series
    suspend fun addSeriesToFavorites(series: Series)
    suspend fun removeSeriesFromFavorites(seriesId: Int)
    fun getFavoriteSeries(): Flow<List<Series>>
    fun isSeriesFavorite(seriesId: Int): Flow<Boolean>
    suspend fun toggleSeriesFavorite(series: Series)



    // Cache operations
    suspend fun cacheMovies(movies: List<Movie>)
    suspend fun cacheSeries(series: List<Series>)
    suspend fun getCachedMovies(): Flow<List<Movie>>
    suspend fun getCachedSeries(): Flow<List<Series>>
}
