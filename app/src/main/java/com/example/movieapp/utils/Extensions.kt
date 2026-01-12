package com.example.movieapp.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * Extension functions for common operations
 */

// Context Extensions
fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

// String Extensions
fun String.toImageUrl(size: String = Constants.POSTER_SIZE_W500): String {
    return if (this.isNotEmpty()) {
        "${Constants.TMDB_IMAGE_BASE_URL}$size$this"
    } else {
        ""
    }
}

fun String.orDefault(default: String = "N/A"): String {
    return this.ifEmpty { default }
}

// Double Extensions
fun Double.formatRating(): String {
    return String.format("%.1f", this)
}

// Composable Extensions
@Composable
fun ShowToast(message: String) {
    val context = LocalContext.current
    context.showToast(message)
}
