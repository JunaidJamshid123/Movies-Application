package com.example.movieapp.presentation.ui.person

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.model.PersonCredit
import com.example.movieapp.domain.model.PersonDetails
import com.example.movieapp.domain.usecase.GetPersonCreditsUseCase
import com.example.movieapp.domain.usecase.GetPersonDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PersonDetailsUiState(
    val personDetails: PersonDetails? = null,
    val movieCredits: List<PersonCredit> = emptyList(),
    val tvCredits: List<PersonCredit> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class PersonDetailsViewModel @Inject constructor(
    private val getPersonDetailsUseCase: GetPersonDetailsUseCase,
    private val getPersonCreditsUseCase: GetPersonCreditsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PersonDetailsUiState())
    val uiState: StateFlow<PersonDetailsUiState> = _uiState.asStateFlow()

    fun loadPersonDetails(personId: Int) {
        viewModelScope.launch {
            try {
                _uiState.value = PersonDetailsUiState(isLoading = true)
                
                val details = getPersonDetailsUseCase(personId)
                val credits = getPersonCreditsUseCase(personId)
                
                _uiState.value = PersonDetailsUiState(
                    personDetails = details,
                    movieCredits = credits.movieCredits.take(20),
                    tvCredits = credits.tvCredits.take(20),
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = PersonDetailsUiState(
                    isLoading = false,
                    error = e.message ?: "An unknown error occurred"
                )
            }
        }
    }
}
