package com.example.movieapp.presentation.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.Series
import com.example.movieapp.domain.usecase.GetFavoriteMoviesUseCase
import com.example.movieapp.domain.usecase.GetFavoriteSeriesUseCase
import com.example.movieapp.domain.usecase.RemoveMovieFromFavoritesUseCase
import com.example.movieapp.domain.usecase.RemoveSeriesFromFavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class FavoriteTab {
    MOVIES, SERIES
}

data class FavoritesUiState(
    val favoriteMovies: List<Movie> = emptyList(),
    val favoriteSeries: List<Series> = emptyList(),
    val selectedTab: FavoriteTab = FavoriteTab.MOVIES,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
    private val getFavoriteSeriesUseCase: GetFavoriteSeriesUseCase,
    private val removeMovieFromFavoritesUseCase: RemoveMovieFromFavoritesUseCase,
    private val removeSeriesFromFavoritesUseCase: RemoveSeriesFromFavoritesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                combine(
                    getFavoriteMoviesUseCase(),
                    getFavoriteSeriesUseCase()
                ) { movies, series ->
                    _uiState.update {
                        it.copy(
                            favoriteMovies = movies,
                            favoriteSeries = series,
                            isLoading = false,
                            error = null
                        )
                    }
                }.collect()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load favorites"
                    )
                }
            }
        }
    }

    fun selectTab(tab: FavoriteTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun removeMovieFromFavorites(movieId: Int) {
        viewModelScope.launch {
            try {
                removeMovieFromFavoritesUseCase(movieId)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = "Failed to remove movie: ${e.message}")
                }
            }
        }
    }

    fun removeSeriesFromFavorites(seriesId: Int) {
        viewModelScope.launch {
            try {
                removeSeriesFromFavoritesUseCase(seriesId)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = "Failed to remove series: ${e.message}")
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
