package com.example.movieapp.domain.model

data class Video(
    val id: String,
    val key: String,
    val name: String,
    val site: String,
    val type: String,
    val official: Boolean
) {
    val youtubeUrl: String
        get() = if (site == "YouTube") "https://www.youtube.com/watch?v=$key" else ""
    
    val youtubeThumbnailUrl: String
        get() = if (site == "YouTube") "https://img.youtube.com/vi/$key/hqdefault.jpg" else ""
    
    val youtubeEmbedUrl: String
        get() = if (site == "YouTube") "https://www.youtube.com/embed/$key" else ""
}
