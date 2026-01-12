package com.example.movieapp.presentation.ui.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.Series
import com.example.movieapp.domain.usecase.DiscoverMoviesUseCase
import com.example.movieapp.domain.usecase.DiscoverSeriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Mood categories with predefined genre combinations
enum class MoodCategory(
    val displayName: String,
    val movieGenres: String,
    val seriesGenres: String,
    val emoji: String,
    val description: String
) {
    FEEL_GOOD(
        displayName = "Feel Good",
        movieGenres = "35,10751", // Comedy, Family
        seriesGenres = "35,10751",
        emoji = "😊",
        description = "Light-hearted and uplifting"
    ),
    THRILLING(
        displayName = "Thrilling",
        movieGenres = "53,28,80", // Thriller, Action, Crime
        seriesGenres = "80,9648,10759",
        emoji = "😱",
        description = "Edge-of-your-seat excitement"
    ),
    MIND_BENDING(
        displayName = "Mind-Bending",
        movieGenres = "878,9648", // Sci-Fi, Mystery
        seriesGenres = "10765,9648",
        emoji = "🤯",
        description = "Twist your perception"
    ),
    ROMANTIC(
        displayName = "Romantic",
        movieGenres = "10749", // Romance
        seriesGenres = "10749",
        emoji = "💕",
        description = "Love stories that touch your heart"
    ),
    SPOOKY(
        displayName = "Spooky",
        movieGenres = "27,9648", // Horror, Mystery
        seriesGenres = "9648",
        emoji = "👻",
        description = "Chills and thrills"
    ),
    EPIC_ADVENTURE(
        displayName = "Epic Adventure",
        movieGenres = "12,14", // Adventure, Fantasy
        seriesGenres = "10759,10765",
        emoji = "⚔️",
        description = "Grand journeys and quests"
    ),
    DOCUMENTARIES(
        displayName = "Learn Something",
        movieGenres = "99", // Documentary
        seriesGenres = "99",
        emoji = "🎓",
        description = "Real stories, real insights"
    ),
    ANIMATED(
        displayName = "Animated",
        movieGenres = "16",
        seriesGenres = "16",
        emoji = "🎨",
        description = "Beautiful animation for all ages"
    )
}

data class FilterOptions(
    val yearFrom: Int? = null,
    val yearTo: Int? = null,
    val ratingMin: Float? = null,
    val ratingMax: Float? = null,
    val runtimeMin: Int? = null,
    val runtimeMax: Int? = null,
    val selectedGenres: List<Int> = emptyList(),
    val language: String? = null,
    val sortBy: String = "popularity.desc"
)

data class DiscoverUiState(
    val movies: List<Movie> = emptyList(),
    val series: List<Series> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val activeFilters: FilterOptions = FilterOptions(),
    val activeMood: MoodCategory? = null,
    val contentType: ContentType = ContentType.MOVIES
)

enum class ContentType {
    MOVIES, SERIES
}

// Genre mappings (TMDB genre IDs)
object GenreData {
    val movieGenres = mapOf(
        28 to "Action",
        12 to "Adventure",
        16 to "Animation",
        35 to "Comedy",
        80 to "Crime",
        99 to "Documentary",
        18 to "Drama",
        10751 to "Family",
        14 to "Fantasy",
        36 to "History",
        27 to "Horror",
        10402 to "Music",
        9648 to "Mystery",
        10749 to "Romance",
        878 to "Science Fiction",
        10770 to "TV Movie",
        53 to "Thriller",
        10752 to "War",
        37 to "Western"
    )
    
    val seriesGenres = mapOf(
        10759 to "Action & Adventure",
        16 to "Animation",
        35 to "Comedy",
        80 to "Crime",
        99 to "Documentary",
        18 to "Drama",
        10751 to "Family",
        10762 to "Kids",
        9648 to "Mystery",
        10763 to "News",
        10764 to "Reality",
        10765 to "Sci-Fi & Fantasy",
        10766 to "Soap",
        10767 to "Talk",
        10768 to "War & Politics",
        37 to "Western"
    )
    
    val languages = mapOf(
        "en" to "English",
        "es" to "Spanish",
        "fr" to "French",
        "de" to "German",
        "it" to "Italian",
        "ja" to "Japanese",
        "ko" to "Korean",
        "zh" to "Chinese",
        "hi" to "Hindi",
        "pt" to "Portuguese"
    )
    
    val sortOptions = mapOf(
        "popularity.desc" to "Most Popular",
        "vote_average.desc" to "Highest Rated",
        "primary_release_date.desc" to "Newest First",
        "primary_release_date.asc" to "Oldest First",
        "revenue.desc" to "Highest Grossing"
    )
}

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val discoverMoviesUseCase: DiscoverMoviesUseCase,
    private val discoverSeriesUseCase: DiscoverSeriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DiscoverUiState())
    val uiState: StateFlow<DiscoverUiState> = _uiState.asStateFlow()

    init {
        loadPopularContent()
    }

    private fun loadPopularContent() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val movies = discoverMoviesUseCase()
                _uiState.value = _uiState.value.copy(
                    movies = movies,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun selectMood(mood: MoodCategory) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                activeMood = mood,
                activeFilters = FilterOptions()
            )
            try {
                when (_uiState.value.contentType) {
                    ContentType.MOVIES -> {
                        val movies = discoverMoviesUseCase(genres = mood.movieGenres)
                        _uiState.value = _uiState.value.copy(
                            movies = movies,
                            isLoading = false
                        )
                    }
                    ContentType.SERIES -> {
                        val series = discoverSeriesUseCase(genres = mood.seriesGenres)
                        _uiState.value = _uiState.value.copy(
                            series = series,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun applyFilters(filters: FilterOptions) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                activeFilters = filters,
                activeMood = null
            )
            try {
                val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
                val genresString = if (filters.selectedGenres.isNotEmpty()) {
                    filters.selectedGenres.joinToString(",")
                } else null

                when (_uiState.value.contentType) {
                    ContentType.MOVIES -> {
                        val movies = discoverMoviesUseCase(
                            genres = genresString,
                            releaseDateGte = filters.yearFrom?.let { "$it-01-01" },
                            releaseDateLte = filters.yearTo?.let { "$it-12-31" },
                            voteAverageGte = filters.ratingMin,
                            voteAverageLte = filters.ratingMax,
                            runtimeGte = filters.runtimeMin,
                            runtimeLte = filters.runtimeMax,
                            language = filters.language,
                            sortBy = filters.sortBy
                        )
                        _uiState.value = _uiState.value.copy(
                            movies = movies,
                            isLoading = false
                        )
                    }
                    ContentType.SERIES -> {
                        val series = discoverSeriesUseCase(
                            genres = genresString,
                            firstAirDateGte = filters.yearFrom?.let { "$it-01-01" },
                            firstAirDateLte = filters.yearTo?.let { "$it-12-31" },
                            voteAverageGte = filters.ratingMin,
                            voteAverageLte = filters.ratingMax,
                            language = filters.language,
                            sortBy = filters.sortBy
                        )
                        _uiState.value = _uiState.value.copy(
                            series = series,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun setContentType(type: ContentType) {
        _uiState.value = _uiState.value.copy(contentType = type)
        if (_uiState.value.activeMood != null) {
            selectMood(_uiState.value.activeMood!!)
        } else if (_uiState.value.activeFilters != FilterOptions()) {
            applyFilters(_uiState.value.activeFilters)
        } else {
            loadPopularContent()
        }
    }

    fun clearFilters() {
        _uiState.value = _uiState.value.copy(
            activeFilters = FilterOptions(),
            activeMood = null
        )
        loadPopularContent()
    }
}
