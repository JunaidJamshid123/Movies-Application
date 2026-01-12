package com.example.movieapp.presentation.ui.series
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.model.Series
import com.example.movieapp.domain.usecase.GetPopularSeriesUseCase
import com.example.movieapp.domain.usecase.GetTrendingSeriesUseCase
import com.example.movieapp.domain.usecase.SearchSeriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SeriesUiState(
    val popularSeries: List<Series> = emptyList(),
    val trendingSeries: List<Series> = emptyList(),
    val searchResults: List<Series> = emptyList(),
    val searchQuery: String = "",
    val isSearching: Boolean = false,
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class SeriesViewModel @Inject constructor(
    private val getPopularSeries: GetPopularSeriesUseCase,
    private val getTrendingSeries: GetTrendingSeriesUseCase,
    private val searchSeriesUseCase: SearchSeriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SeriesUiState())
    val uiState: StateFlow<SeriesUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            try {
                _uiState.value = SeriesUiState(isLoading = true)

                val popularSeries = getPopularSeries()
                val trendingSeries = getTrendingSeries()

                _uiState.value = SeriesUiState(
                    popularSeries = popularSeries,
                    trendingSeries = trendingSeries,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = SeriesUiState(
                    isLoading = false,
                    error = e.message ?: "An unknown error occurred"
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
                val results = searchSeriesUseCase(query)
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

    fun retry() {
        fetchData()
    }

    fun refresh() {
        fetchData()
    }
}
