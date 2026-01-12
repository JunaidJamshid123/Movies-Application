package com.example.movieapp.presentation.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")  // Add this line
    object Home : Screen("home")
    object Movies : Screen("movies")
    object Series : Screen("series")
    object Favorites : Screen("favorites")
    object Profile : Screen("profile")
    object Discover : Screen("discover")
    object MovieDetails : Screen("movie_details/{movieId}") {
        fun createRoute(movieId: Int) = "movie_details/$movieId"
    }
    object SeriesDetails : Screen("series_details/{seriesId}") {
        fun createRoute(seriesId: Int) = "series_details/$seriesId"
    }
    object PersonDetails : Screen("person_details/{personId}") {
        fun createRoute(personId: Int) = "person_details/$personId"
    }
}
