package com.example.movieapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CreditsResponseDto(
    @SerializedName("id") val id: Int,
    @SerializedName("cast") val cast: List<CastDto>,
    @SerializedName("crew") val crew: List<CrewDto>
)

data class CastDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("character") val character: String?,
    @SerializedName("profile_path") val profilePath: String?,
    @SerializedName("order") val order: Int,
    @SerializedName("known_for_department") val knownForDepartment: String?
)

data class CrewDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("job") val job: String,
    @SerializedName("department") val department: String,
    @SerializedName("profile_path") val profilePath: String?
)

// Person details for when clicking on cast/crew
data class PersonDetailsDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("biography") val biography: String?,
    @SerializedName("birthday") val birthday: String?,
    @SerializedName("deathday") val deathday: String?,
    @SerializedName("place_of_birth") val placeOfBirth: String?,
    @SerializedName("profile_path") val profilePath: String?,
    @SerializedName("known_for_department") val knownForDepartment: String?,
    @SerializedName("popularity") val popularity: Double
)

data class PersonCreditsDto(
    @SerializedName("id") val id: Int,
    @SerializedName("cast") val cast: List<PersonCastCreditDto>,
    @SerializedName("crew") val crew: List<PersonCrewCreditDto>
)

data class PersonCastCreditDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String?, // For movies
    @SerializedName("name") val name: String?, // For TV shows
    @SerializedName("character") val character: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("first_air_date") val firstAirDate: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("media_type") val mediaType: String?
)

data class PersonCrewCreditDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("job") val job: String,
    @SerializedName("department") val department: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("first_air_date") val firstAirDate: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("media_type") val mediaType: String?
)
