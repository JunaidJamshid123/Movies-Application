package com.example.movieapp.presentation.ui.components

import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.rememberAsyncImagePainter
import com.example.movieapp.domain.model.Video
import com.example.movieapp.presentation.ui.theme.DarkBackground
import com.example.movieapp.presentation.ui.theme.MovieRed

@Composable
fun TrailerSection(
    trailer: Video?,
    onPlayClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (trailer == null) return
    
    val context = LocalContext.current
    
    Column(modifier = modifier) {
        Text(
            text = "Trailer",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ),
            color = Color.White,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable { 
                    // Open YouTube app or browser
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(trailer.youtubeUrl))
                    intent.putExtra("force_fullscreen", true)
                    // Try to open in YouTube app first
                    intent.setPackage("com.google.android.youtube")
                    try {
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        // Fall back to browser if YouTube app not installed
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(trailer.youtubeUrl))
                        context.startActivity(browserIntent)
                    }
                }
        ) {
            // Thumbnail
            Image(
                painter = rememberAsyncImagePainter(trailer.youtubeThumbnailUrl),
                contentDescription = trailer.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            
            // Dark overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
            )
            
            // Play button
            Surface(
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.Center),
                shape = CircleShape,
                color = MovieRed
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play Trailer",
                    tint = Color.White,
                    modifier = Modifier
                        .padding(16.dp)
                        .size(32.dp)
                )
            }
            
            // Trailer name
            Text(
                text = trailer.name,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            )
        }
    }
}

@Composable
fun YouTubePlayerDialog(
    videoKey: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    
    // Just open in YouTube app/browser directly and dismiss
    LaunchedEffect(videoKey) {
        val youtubeUrl = "https://www.youtube.com/watch?v=$videoKey"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl))
        intent.putExtra("force_fullscreen", true)
        // Try to open in YouTube app first
        intent.setPackage("com.google.android.youtube")
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            // Fall back to browser if YouTube app not installed
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl))
            context.startActivity(browserIntent)
        }
        onDismiss()
    }
}

@Composable
fun YouTubeWebView(
    videoKey: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    val htmlContent = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
            <style>
                * { margin: 0; padding: 0; }
                html, body { width: 100%; height: 100%; background-color: #000; }
                iframe { width: 100%; height: 100%; border: none; }
            </style>
        </head>
        <body>
            <iframe 
                src="https://www.youtube.com/embed/$videoKey?autoplay=1&rel=0&showinfo=0&modestbranding=1&playsinline=1"
                frameborder="0"
                allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                allowfullscreen>
            </iframe>
        </body>
        </html>
    """.trimIndent()
    
    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            WebView(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    mediaPlaybackRequiresUserGesture = false
                    loadWithOverviewMode = true
                    useWideViewPort = true
                    cacheMode = WebSettings.LOAD_DEFAULT
                }
                
                webChromeClient = WebChromeClient()
                webViewClient = WebViewClient()
                
                setBackgroundColor(android.graphics.Color.BLACK)
            }
        },
        update = { webView ->
            webView.loadDataWithBaseURL(
                "https://www.youtube.com",
                htmlContent,
                "text/html",
                "UTF-8",
                null
            )
        }
    )
}
