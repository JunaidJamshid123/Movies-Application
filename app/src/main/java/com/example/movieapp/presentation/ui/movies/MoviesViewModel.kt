package com.example.movieapp.presentation.ui.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.Series
import com.example.movieapp.domain.usecase.GetFavoriteMoviesUseCase
import com.example.movieapp.domain.usecase.GetPopularMoviesUseCase
import com.example.movieapp.domain.usecase.GetPopularSeriesUseCase
import com.example.movieapp.domain.usecase.GetTrendingMoviesUseCase
import com.example.movieapp.domain.usecase.GetTrendingSeriesUseCase
import com.example.movieapp.domain.usecase.SearchMoviesUseCase
import com.example.movieapp.domain.usecase.ToggleMovieFavoriteUseCase
import com.example.movieapp.presentation.ui.home.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MoviesUiState(
    val heroItems: List<Movie> = emptyList(),
    val popularMovies: List<Movie> = emptyList(),
    val trendingMovies: List<Movie> = emptyList(),
    val searchResults: List<Movie> = emptyList(),
    val favoriteMovieIds: Set<Int> = emptySet(),
    val searchQuery: String = "",
    val isSearching: Boolean = false,
    val isLoading: Boolean = true,
    val error: String? = null
)
@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val getPopularMovies: GetPopularMoviesUseCase,
    private val getTrendingMovies: GetTrendingMoviesUseCase,
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val toggleMovieFavoriteUseCase: ToggleMovieFavoriteUseCase,
    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MoviesUiState())
    val uiState: StateFlow<MoviesUiState> = _uiState

    private var searchJob: Job? = null

    init {
        fetchData()
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            getFavoriteMoviesUseCase().collectLatest { favoriteMovies ->
                _uiState.value = _uiState.value.copy(
                    favoriteMovieIds = favoriteMovies.map { it.id }.toSet()
                )
            }
        }
    }

    fun toggleFavorite(movie: Movie) {
        viewModelScope.launch {
            toggleMovieFavoriteUseCase(movie)
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            try {
                _uiState.value = MoviesUiState(isLoading = true)

                val trendingMovies = getTrendingMovies()
                val popularMovies = getPopularMovies()

                _uiState.value = _uiState.value.copy(
                    heroItems = trendingMovies.take(5), // Top 5 for hero carousel
                    popularMovies = popularMovies,
                    trendingMovies = trendingMovies,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = MoviesUiState(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        
        searchJob?.cancel()
        
        if (query.isBlank()) {
            _uiState.value = _uiState.value.copy(
                searchResults = emptyList(),
                isSearching = false
            )
            return
        }

        searchJob = viewModelScope.launch {
            delay(300) // Debounce
            _uiState.value = _uiState.value.copy(isSearching = true)
            try {
                val results = searchMoviesUseCase(query)
                _uiState.value = _uiState.value.copy(
                    searchResults = results,
                    isSearching = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSearching = false,
                    error = e.message
                )
            }
        }
    }

    fun clearSearch() {
        _uiState.value = _uiState.value.copy(
            searchQuery = "",
            searchResults = emptyList(),
            isSearching = false
        )
    }
}
