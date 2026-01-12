package com.example.movieapp.data.repository

import com.example.movieapp.data.local.dao.MovieDao
import com.example.movieapp.data.local.dao.SeriesDao
import com.example.movieapp.data.local.mapper.toDomain
import com.example.movieapp.data.local.mapper.toEntity
import com.example.movieapp.data.remote.api.TMDBApi
import com.example.movieapp.data.remote.mapper.toDomain
import com.example.movieapp.domain.model.Credits
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.MovieDetails
import com.example.movieapp.domain.model.PersonCredits
import com.example.movieapp.domain.model.PersonDetails
import com.example.movieapp.domain.model.Series
import com.example.movieapp.domain.model.SeriesDetails
import com.example.movieapp.domain.model.Video
import com.example.movieapp.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val api: TMDBApi,
    private val movieDao: MovieDao,
    private val seriesDao: SeriesDao
) : MovieRepository {

    override suspend fun getPopularMovies(): List<Movie> {
        val response = api.getPopularMovies()
        return response.results.map { it.toDomain() }
    }

    override suspend fun getTopRatedMovies(): List<Movie> {
        val response = api.getTopRatedMovies()
        return response.results.map { it.toDomain() }
    }

    override suspend fun getPopularSeries(): List<Series> {
        val response = api.getPopularSeries()
        return response.results.map { it.toDomain() }
    }

    override suspend fun getTopRatedSeries(): List<Series> {
        val response = api.getTopRatedSeries()
        return response.results.map { it.toDomain() }
    }
    
    override suspend fun getTrendingMovies(): List<Movie> {
        val response = api.getTrendingMovies()
        return response.results.map { it.toDomain() }
    }
    
    override suspend fun getTrendingSeries(): List<Series> {
        val response = api.getTrendingSeries()
        return response.results.map { it.toDomain() }
    }

    override suspend fun getMovieDetails(movieId: Int): MovieDetails {
        val response = api.getMovieDetails(movieId)
        return response.toDomain()
    }

    override suspend fun getSeriesDetails(seriesId: Int): SeriesDetails {
        val response = api.getSeriesDetails(seriesId)
        return response.toDomain()
    }

    override suspend fun getMovieVideos(movieId: Int): List<Video> {
        val response = api.getMovieVideos(movieId)
        return response.results.map { it.toDomain() }
    }

    override suspend fun getSeriesVideos(seriesId: Int): List<Video> {
        val response = api.getSeriesVideos(seriesId)
        return response.results.map { it.toDomain() }
    }

    // ========== CREDITS ==========

    override suspend fun getMovieCredits(movieId: Int): Credits {
        val response = api.getMovieCredits(movieId)
        return response.toDomain()
    }

    override suspend fun getSeriesCredits(seriesId: Int): Credits {
        val response = api.getSeriesCredits(seriesId)
        return response.toDomain()
    }

    // ========== SIMILAR & RECOMMENDATIONS ==========

    override suspend fun getSimilarMovies(movieId: Int): List<Movie> {
        val response = api.getSimilarMovies(movieId)
        return response.results.map { it.toDomain() }
    }

    override suspend fun getMovieRecommendations(movieId: Int): List<Movie> {
        val response = api.getMovieRecommendations(movieId)
        return response.results.map { it.toDomain() }
    }

    override suspend fun getSimilarSeries(seriesId: Int): List<Series> {
        val response = api.getSimilarSeries(seriesId)
        return response.results.map { it.toDomain() }
    }

    override suspend fun getSeriesRecommendations(seriesId: Int): List<Series> {
        val response = api.getSeriesRecommendations(seriesId)
        return response.results.map { it.toDomain() }
    }

    // ========== PERSON ==========

    override suspend fun getPersonDetails(personId: Int): PersonDetails {
        val response = api.getPersonDetails(personId)
        return response.toDomain()
    }

    override suspend fun getPersonCredits(personId: Int): PersonCredits {
        val response = api.getPersonCredits(personId)
        return response.toDomain()
    }

    // ========== DISCOVER (Filters & Mood) ==========

    override suspend fun discoverMovies(
        genres: String?,
        releaseDateGte: String?,
        releaseDateLte: String?,
        voteAverageGte: Float?,
        voteAverageLte: Float?,
        runtimeGte: Int?,
        runtimeLte: Int?,
        language: String?,
        sortBy: String
    ): List<Movie> {
        val response = api.discoverMovies(
            genres = genres,
            releaseDateGte = releaseDateGte,
            releaseDateLte = releaseDateLte,
            voteAverageGte = voteAverageGte,
            voteAverageLte = voteAverageLte,
            runtimeGte = runtimeGte,
            runtimeLte = runtimeLte,
            language = language,
            sortBy = sortBy
        )
        return response.results.map { it.toDomain() }
    }

    override suspend fun discoverSeries(
        genres: String?,
        firstAirDateGte: String?,
        firstAirDateLte: String?,
        voteAverageGte: Float?,
        voteAverageLte: Float?,
        language: String?,
        sortBy: String
    ): List<Series> {
        val response = api.discoverSeries(
            genres = genres,
            firstAirDateGte = firstAirDateGte,
            firstAirDateLte = firstAirDateLte,
            voteAverageGte = voteAverageGte,
            voteAverageLte = voteAverageLte,
            language = language,
            sortBy = sortBy
        )
        return response.results.map { it.toDomain() }
    }

    // ========== SEARCH ==========

    override suspend fun searchMovies(query: String): List<Movie> {
        val response = api.searchMovies(query = query)
        return response.results.map { it.toDomain() }
    }

    override suspend fun searchSeries(query: String): List<Series> {
        val response = api.searchSeries(query = query)
        return response.results.map { it.toDomain() }
    }

    // ========== FAVORITES - MOVIES ==========
    
    override suspend fun addMovieToFavorites(movie: Movie) {
        val entity = movie.toEntity().copy(isFavorite = true)
        movieDao.insertMovie(entity)
    }

    override suspend fun removeMovieFromFavorites(movieId: Int) {
        movieDao.updateFavoriteStatus(movieId, false)
    }

    override fun getFavoriteMovies(): Flow<List<Movie>> {
        return movieDao.getFavoriteMovies().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun isMovieFavorite(movieId: Int): Flow<Boolean> {
        return movieDao.isMovieFavorite(movieId)
    }

    override suspend fun toggleMovieFavorite(movie: Movie) {
        val entity = movieDao.getMovieById(movie.id)
        if (entity != null && entity.isFavorite) {
            movieDao.updateFavoriteStatus(movie.id, false)
        } else {
            val newEntity = movie.toEntity().copy(isFavorite = true)
            movieDao.insertMovie(newEntity)
        }
    }

    // ========== FAVORITES - SERIES ==========
    
    override suspend fun addSeriesToFavorites(series: Series) {
        val entity = series.toEntity().copy(isFavorite = true)
        seriesDao.insertSeries(entity)
    }

    override suspend fun removeSeriesFromFavorites(seriesId: Int) {
        seriesDao.updateFavoriteStatus(seriesId, false)
    }

    override fun getFavoriteSeries(): Flow<List<Series>> {
        return seriesDao.getFavoriteSeries().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun isSeriesFavorite(seriesId: Int): Flow<Boolean> {
        return seriesDao.isSeriesFavorite(seriesId)
    }

    override suspend fun toggleSeriesFavorite(series: Series) {
        val entity = seriesDao.getSeriesById(series.id)
        if (entity != null && entity.isFavorite) {
            seriesDao.updateFavoriteStatus(series.id, false)
        } else {
            val newEntity = series.toEntity().copy(isFavorite = true)
            seriesDao.insertSeries(newEntity)
        }
    }

    // ========== CACHE OPERATIONS ==========
    
    override suspend fun cacheMovies(movies: List<Movie>) {
        val entities = movies.map { it.toEntity() }
        movieDao.insertMovies(entities)
    }

    override suspend fun cacheSeries(series: List<Series>) {
        val entities = series.map { it.toEntity() }
        seriesDao.insertMultipleSeries(entities)
    }

    override suspend fun getCachedMovies(): Flow<List<Movie>> {
        return movieDao.getAllMovies().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getCachedSeries(): Flow<List<Series>> {
        return seriesDao.getAllSeries().map { entities ->
            entities.map { it.toDomain() }
        }
    }
}
