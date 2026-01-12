package com.example.movieapp.domain.usecase

import com.example.movieapp.domain.model.PersonCredits
import com.example.movieapp.domain.model.PersonDetails
import com.example.movieapp.domain.repository.MovieRepository
import javax.inject.Inject

class GetPersonDetailsUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(personId: Int): PersonDetails {
        return repository.getPersonDetails(personId)
    }
}

class GetPersonCreditsUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(personId: Int): PersonCredits {
        return repository.getPersonCredits(personId)
    }
}
