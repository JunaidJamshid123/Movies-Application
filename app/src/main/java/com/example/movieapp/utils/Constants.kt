package com.example.movieapp.utils

object Constants {
    const val TMDB_API_KEY = "eaf1ede8d19c76f03bafa547230934e8"
    const val TMDB_BASE_URL = "https://api.themoviedb.org/3/"
    const val TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/"
    const val POSTER_SIZE_W500 = "w500"
    const val BACKDROP_SIZE_W780 = "w780"
    
    // Database
    const val DATABASE_NAME = "movie_app_database"
    const val DATABASE_VERSION = 1
    
    // Network
    const val NETWORK_TIMEOUT = 30L
    const val CACHE_SIZE = 10 * 1024 * 1024L // 10 MB
}
