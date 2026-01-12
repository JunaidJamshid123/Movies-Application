package com.example.movieapp.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.Series
import com.example.movieapp.domain.usecase.GetPopularMoviesUseCase
import com.example.movieapp.domain.usecase.GetPopularSeriesUseCase
import com.example.movieapp.domain.usecase.GetTrendingMoviesUseCase
import com.example.movieapp.domain.usecase.GetTrendingSeriesUseCase
import com.example.movieapp.domain.usecase.ToggleMovieFavoriteUseCase
import com.example.movieapp.domain.usecase.ToggleSeriesFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val heroItems: List<Movie> = emptyList(),
    val popularMovies: List<Movie> = emptyList(),
    val popularSeries: List<Series> = emptyList(),
    val trendingMovies: List<Movie> = emptyList(),
    val trendingSeries: List<Series> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPopularMovies: GetPopularMoviesUseCase,
    private val getPopularSeries: GetPopularSeriesUseCase,
    private val getTrendingMovies: GetTrendingMoviesUseCase,
    private val getTrendingSeries: GetTrendingSeriesUseCase,
    private val toggleMovieFavoriteUseCase: ToggleMovieFavoriteUseCase,
    private val toggleSeriesFavoriteUseCase: ToggleSeriesFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            try {
                _uiState.value = HomeUiState(isLoading = true)

                val trendingMovies = getTrendingMovies()
                val popularMovies = getPopularMovies()
                val popularSeries = getPopularSeries()
                val trendingSeries = getTrendingSeries()

                _uiState.value = HomeUiState(
                    heroItems = trendingMovies.take(5), // Top 5 for hero carousel
                    popularMovies = popularMovies,
                    popularSeries = popularSeries,
                    trendingMovies = trendingMovies,
                    trendingSeries = trendingSeries,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = HomeUiState(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun toggleMovieFavorite(movie: Movie) {
        viewModelScope.launch {
            try {
                toggleMovieFavoriteUseCase(movie)
            } catch (e: Exception) {
                // Handle error if needed
            }
        }
    }
    
    fun toggleSeriesFavorite(series: Series) {
        viewModelScope.launch {
            try {
                toggleSeriesFavoriteUseCase(series)
            } catch (e: Exception) {
                // Handle error if needed
            }
        }
    }
}
