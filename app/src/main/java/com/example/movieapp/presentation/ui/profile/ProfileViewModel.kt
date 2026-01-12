package com.example.movieapp.presentation.ui.profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class ProfileUiState(
    val userName: String = "Movie Enthusiast",
    val userEmail: String = "user@movieapp.com",
    val notificationsEnabled: Boolean = true,
    val autoPlayEnabled: Boolean = false,
    val darkModeEnabled: Boolean = true
)

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
    
    fun toggleNotifications() {
        _uiState.value = _uiState.value.copy(
            notificationsEnabled = !_uiState.value.notificationsEnabled
        )
    }
    
    fun toggleAutoPlay() {
        _uiState.value = _uiState.value.copy(
            autoPlayEnabled = !_uiState.value.autoPlayEnabled
        )
    }
    
    fun toggleDarkMode() {
        _uiState.value = _uiState.value.copy(
            darkModeEnabled = !_uiState.value.darkModeEnabled
        )
    }
}
