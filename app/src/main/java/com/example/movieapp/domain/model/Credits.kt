package com.example.movieapp.domain.model

data class Cast(
    val id: Int,
    val name: String,
    val character: String,
    val profileUrl: String?,
    val order: Int,
    val department: String
)

data class Crew(
    val id: Int,
    val name: String,
    val job: String,
    val department: String,
    val profileUrl: String?
)

data class Credits(
    val cast: List<Cast>,
    val crew: List<Crew>
) {
    val directors: List<Crew>
        get() = crew.filter { it.job == "Director" }
    
    val writers: List<Crew>
        get() = crew.filter { it.department == "Writing" }
    
    val producers: List<Crew>
        get() = crew.filter { it.job == "Producer" || it.job == "Executive Producer" }
}

data class PersonDetails(
    val id: Int,
    val name: String,
    val biography: String,
    val birthday: String?,
    val deathday: String?,
    val placeOfBirth: String?,
    val profileUrl: String?,
    val knownFor: String,
    val popularity: Double
)

data class PersonCredit(
    val id: Int,
    val title: String,
    val character: String?,
    val job: String?,
    val posterUrl: String?,
    val releaseDate: String,
    val voteAverage: Double,
    val mediaType: String // "movie" or "tv"
)

data class PersonCredits(
    val cast: List<PersonCredit>,
    val crew: List<PersonCredit>
) {
    val allCredits: List<PersonCredit>
        get() = (cast + crew).distinctBy { it.id }.sortedByDescending { it.releaseDate }
    
    val movieCredits: List<PersonCredit>
        get() = allCredits.filter { it.mediaType == "movie" }
    
    val tvCredits: List<PersonCredit>
        get() = allCredits.filter { it.mediaType == "tv" }
}
