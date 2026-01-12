package com.example.movieapp.presentation.ui.series

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.model.Cast
import com.example.movieapp.domain.model.Credits
import com.example.movieapp.domain.model.Crew
import com.example.movieapp.domain.model.Series
import com.example.movieapp.domain.model.SeriesDetails
import com.example.movieapp.domain.model.Video
import com.example.movieapp.domain.usecase.GetSeriesCreditsUseCase
import com.example.movieapp.domain.usecase.GetSeriesDetailsUseCase
import com.example.movieapp.domain.usecase.GetSeriesVideosUseCase
import com.example.movieapp.domain.usecase.GetSimilarSeriesUseCase
import com.example.movieapp.domain.usecase.IsSeriesFavoriteUseCase
import com.example.movieapp.domain.usecase.ToggleSeriesFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SeriesDetailsUiState(
    val seriesDetails: SeriesDetails? = null,
    val videos: List<Video> = emptyList(),
    val trailer: Video? = null,
    val cast: List<Cast> = emptyList(),
    val crew: List<Crew> = emptyList(),
    val creators: List<Crew> = emptyList(),
    val similarSeries: List<Series> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val isFavorite: Boolean = false
)

@HiltViewModel
class SeriesDetailsViewModel @Inject constructor(
    private val getSeriesDetailsUseCase: GetSeriesDetailsUseCase,
    private val getSeriesVideosUseCase: GetSeriesVideosUseCase,
    private val getSeriesCreditsUseCase: GetSeriesCreditsUseCase,
    private val getSimilarSeriesUseCase: GetSimilarSeriesUseCase,
    private val isSeriesFavoriteUseCase: IsSeriesFavoriteUseCase,
    private val toggleSeriesFavoriteUseCase: ToggleSeriesFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SeriesDetailsUiState())
    val uiState: StateFlow<SeriesDetailsUiState> = _uiState.asStateFlow()

    fun loadSeriesDetails(seriesId: Int) {
        viewModelScope.launch {
            try {
                _uiState.value = SeriesDetailsUiState(isLoading = true)
                val details = getSeriesDetailsUseCase(seriesId)
                
                // Load videos
                val videos = try {
                    getSeriesVideosUseCase(seriesId)
                } catch (e: Exception) {
                    emptyList()
                }
                
                // Load credits (cast & crew)
                val credits = try {
                    getSeriesCreditsUseCase(seriesId)
                } catch (e: Exception) {
                    Credits(emptyList(), emptyList())
                }
                
                // Load similar series
                val similarSeries = try {
                    getSimilarSeriesUseCase(seriesId)
                } catch (e: Exception) {
                    emptyList()
                }
                
                // Find the best trailer (official YouTube trailer preferred)
                val trailer = videos
                    .filter { it.site == "YouTube" && (it.type == "Trailer" || it.type == "Teaser") }
                    .sortedByDescending { it.official }
                    .firstOrNull()
                
                // Get creators/showrunners
                val creators = credits.crew.filter { 
                    it.job == "Creator" || it.job == "Executive Producer" || it.job == "Showrunner"
                }.distinctBy { it.id }
                
                // Observe favorite status
                isSeriesFavoriteUseCase(seriesId).collect { isFav ->
                    _uiState.value = _uiState.value.copy(
                        seriesDetails = details,
                        videos = videos,
                        trailer = trailer,
                        cast = credits.cast.take(20),
                        crew = credits.crew,
                        creators = creators.take(5),
                        similarSeries = similarSeries.take(10),
                        isLoading = false,
                        error = null,
                        isFavorite = isFav
                    )
                }
            } catch (e: Exception) {
                _uiState.value = SeriesDetailsUiState(
                    isLoading = false,
                    error = e.message ?: "An unknown error occurred"
                )
            }
        }
    }
    
    fun toggleFavorite() {
        viewModelScope.launch {
            try {
                _uiState.value.seriesDetails?.let { details ->
                    val series = Series(
                        id = details.id,
                        name = details.name,
                        overview = details.overview,
                        posterUrl = details.posterUrl,
                        backdropUrl = details.backdropUrl ?: "",
                        voteAverage = details.voteAverage,
                        firstAirDate = details.firstAirDate
                    )
                    toggleSeriesFavoriteUseCase(series)
                }
            } catch (e: Exception) {
                // Handle error if needed
            }
        }
    }
}
