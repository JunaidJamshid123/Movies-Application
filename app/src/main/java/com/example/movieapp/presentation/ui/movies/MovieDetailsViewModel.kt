package com.example.movieapp.presentation.ui.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.model.Cast
import com.example.movieapp.domain.model.Credits
import com.example.movieapp.domain.model.Crew
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.MovieDetails
import com.example.movieapp.domain.model.Video
import com.example.movieapp.domain.usecase.GetMovieCreditsUseCase
import com.example.movieapp.domain.usecase.GetMovieDetailsUseCase
import com.example.movieapp.domain.usecase.GetMovieVideosUseCase
import com.example.movieapp.domain.usecase.GetSimilarMoviesUseCase
import com.example.movieapp.domain.usecase.IsMovieFavoriteUseCase
import com.example.movieapp.domain.usecase.ToggleMovieFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MovieDetailsUiState(
    val movieDetails: MovieDetails? = null,
    val videos: List<Video> = emptyList(),
    val trailer: Video? = null,
    val cast: List<Cast> = emptyList(),
    val crew: List<Crew> = emptyList(),
    val directors: List<Crew> = emptyList(),
    val similarMovies: List<Movie> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val isFavorite: Boolean = false
)

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getMovieVideosUseCase: GetMovieVideosUseCase,
    private val getMovieCreditsUseCase: GetMovieCreditsUseCase,
    private val getSimilarMoviesUseCase: GetSimilarMoviesUseCase,
    private val isMovieFavoriteUseCase: IsMovieFavoriteUseCase,
    private val toggleMovieFavoriteUseCase: ToggleMovieFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovieDetailsUiState())
    val uiState: StateFlow<MovieDetailsUiState> = _uiState.asStateFlow()

    fun loadMovieDetails(movieId: Int) {
        viewModelScope.launch {
            try {
                _uiState.value = MovieDetailsUiState(isLoading = true)
                val details = getMovieDetailsUseCase(movieId)
                
                // Load videos
                val videos = try {
                    getMovieVideosUseCase(movieId)
                } catch (e: Exception) {
                    emptyList()
                }
                
                // Load credits (cast & crew)
                val credits = try {
                    getMovieCreditsUseCase(movieId)
                } catch (e: Exception) {
                    Credits(emptyList(), emptyList())
                }
                
                // Load similar movies
                val similarMovies = try {
                    getSimilarMoviesUseCase(movieId)
                } catch (e: Exception) {
                    emptyList()
                }
                
                // Find the best trailer (official YouTube trailer preferred)
                val trailer = videos
                    .filter { it.site == "YouTube" && (it.type == "Trailer" || it.type == "Teaser") }
                    .sortedByDescending { it.official }
                    .firstOrNull()
                
                // Observe favorite status
                isMovieFavoriteUseCase(movieId).collect { isFav ->
                    _uiState.value = _uiState.value.copy(
                        movieDetails = details,
                        videos = videos,
                        trailer = trailer,
                        cast = credits.cast.take(20), // Top 20 cast members
                        crew = credits.crew,
                        directors = credits.directors,
                        similarMovies = similarMovies.take(10),
                        isLoading = false,
                        error = null,
                        isFavorite = isFav
                    )
                }
            } catch (e: Exception) {
                _uiState.value = MovieDetailsUiState(
                    isLoading = false,
                    error = e.message ?: "An unknown error occurred"
                )
            }
        }
    }
    
    fun toggleFavorite() {
        viewModelScope.launch {
            try {
                _uiState.value.movieDetails?.let { details ->
                    val movie = Movie(
                        id = details.id,
                        title = details.title,
                        overview = details.overview,
                        posterUrl = details.posterUrl,
                        backdropUrl = details.backdropUrl ?: "",
                        voteAverage = details.voteAverage,
                        releaseDate = details.releaseDate
                    )
                    toggleMovieFavoriteUseCase(movie)
                }
            } catch (e: Exception) {
                // Handle error if needed
            }
        }
    }
}
