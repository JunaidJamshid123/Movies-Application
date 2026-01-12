package com.example.movieapp.data.remote.api

// TODO: Implement TMDB API interface

import android.provider.ContactsContract.Contacts
import androidx.compose.ui.unit.Constraints
import com.example.movieapp.data.remote.dto.CreditsResponseDto
import com.example.movieapp.data.remote.dto.MovieDetailsDto
import com.example.movieapp.data.remote.dto.MovieResponseDto
import com.example.movieapp.data.remote.dto.PersonCreditsDto
import com.example.movieapp.data.remote.dto.PersonDetailsDto
import com.example.movieapp.data.remote.dto.SeriesDetailsDto
import com.example.movieapp.data.remote.dto.SeriesResponseDto
import com.example.movieapp.data.remote.dto.VideoResponseDto
import com.example.movieapp.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface TMDBApi {

    companion object {
        const val API_KEY = Constants.TMDB_API_KEY
    }

    // ---------------- MOVIES ----------------
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int = 1
    ): MovieResponseDto

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int = 1
    ): MovieResponseDto

    @GET("trending/movie/day")
    suspend fun getTrendingMovies(
        @Query("api_key") apiKey: String = API_KEY
    ): MovieResponseDto

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @retrofit2.http.Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): MovieDetailsDto

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @retrofit2.http.Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): VideoResponseDto

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @retrofit2.http.Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): CreditsResponseDto

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @retrofit2.http.Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int = 1
    ): MovieResponseDto

    @GET("movie/{movie_id}/recommendations")
    suspend fun getMovieRecommendations(
        @retrofit2.http.Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int = 1
    ): MovieResponseDto

    // ---------------- SERIES / TV ----------------

    @GET("tv/popular")
    suspend fun getPopularSeries(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int = 1
    ): SeriesResponseDto

    @GET("tv/top_rated")
    suspend fun getTopRatedSeries(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int = 1
    ): SeriesResponseDto

    @GET("trending/tv/day")
    suspend fun getTrendingSeries(
        @Query("api_key") apiKey: String = API_KEY
    ): SeriesResponseDto

    @GET("tv/{tv_id}")
    suspend fun getSeriesDetails(
        @retrofit2.http.Path("tv_id") seriesId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): SeriesDetailsDto

    @GET("tv/{tv_id}/videos")
    suspend fun getSeriesVideos(
        @retrofit2.http.Path("tv_id") seriesId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): VideoResponseDto

    @GET("tv/{tv_id}/credits")
    suspend fun getSeriesCredits(
        @retrofit2.http.Path("tv_id") seriesId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): CreditsResponseDto

    @GET("tv/{tv_id}/similar")
    suspend fun getSimilarSeries(
        @retrofit2.http.Path("tv_id") seriesId: Int,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int = 1
    ): SeriesResponseDto

    @GET("tv/{tv_id}/recommendations")
    suspend fun getSeriesRecommendations(
        @retrofit2.http.Path("tv_id") seriesId: Int,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int = 1
    ): SeriesResponseDto

    // ---------------- PERSON ----------------

    @GET("person/{person_id}")
    suspend fun getPersonDetails(
        @retrofit2.http.Path("person_id") personId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): PersonDetailsDto

    @GET("person/{person_id}/combined_credits")
    suspend fun getPersonCredits(
        @retrofit2.http.Path("person_id") personId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): PersonCreditsDto

    // ---------------- SEARCH ----------------

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): MovieResponseDto

    @GET("search/tv")
    suspend fun searchSeries(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): SeriesResponseDto

    // ---------------- DISCOVER (for filters and mood) ----------------

    @GET("discover/movie")
    suspend fun discoverMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int = 1,
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("with_genres") genres: String? = null,
        @Query("primary_release_date.gte") releaseDateGte: String? = null,
        @Query("primary_release_date.lte") releaseDateLte: String? = null,
        @Query("vote_average.gte") voteAverageGte: Float? = null,
        @Query("vote_average.lte") voteAverageLte: Float? = null,
        @Query("with_runtime.gte") runtimeGte: Int? = null,
        @Query("with_runtime.lte") runtimeLte: Int? = null,
        @Query("with_original_language") language: String? = null,
        @Query("vote_count.gte") voteCountGte: Int = 50 // Ensure quality results
    ): MovieResponseDto

    @GET("discover/tv")
    suspend fun discoverSeries(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int = 1,
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("with_genres") genres: String? = null,
        @Query("first_air_date.gte") firstAirDateGte: String? = null,
        @Query("first_air_date.lte") firstAirDateLte: String? = null,
        @Query("vote_average.gte") voteAverageGte: Float? = null,
        @Query("vote_average.lte") voteAverageLte: Float? = null,
        @Query("with_original_language") language: String? = null,
        @Query("vote_count.gte") voteCountGte: Int = 50
    ): SeriesResponseDto
}
