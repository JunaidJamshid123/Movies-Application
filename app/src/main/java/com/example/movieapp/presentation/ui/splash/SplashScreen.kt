package com.example.movieapp.presentation.ui.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movieapp.R
import com.example.movieapp.presentation.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.util.Log

@Composable
fun SplashScreen(
    onNavigateToHome: () -> Unit = {}
) {
    // Animations
    val logoScale = remember { Animatable(0.3f) }
    val logoAlpha = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }
    val shimmerAlpha = remember { Animatable(0.3f) }

    LaunchedEffect(key1 = true) {
        Log.d("SplashScreen", "Animation started")
        
        // Start animations in parallel
        launch {
            logoAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(600, easing = FastOutSlowInEasing)
            )
        }
        
        launch {
            logoScale.animateTo(
                targetValue = 1.1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            logoScale.animateTo(
                targetValue = 1f,
                animationSpec = tween(300)
            )
        }
        
        launch {
            textAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(800)
            )
        }
        
        launch {
            shimmerAlpha.animateTo(
                targetValue = 0.8f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1500),
                    repeatMode = RepeatMode.Reverse
                )
            )
        }
        
        // Wait 2.5 seconds, then navigate
        delay(2500L)
        Log.d("SplashScreen", "Navigating to home")
        onNavigateToHome()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Background Image with gradient overlay
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.splash_background),
                contentDescription = "Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.2f)
            )
            
            // Gradient overlay for depth
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                DarkBackground.copy(alpha = 0.7f),
                                DarkBackground.copy(alpha = 0.9f),
                                DarkBackground
                            )
                        )
                    )
            )
        }

        // Center content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Play icon in circle (Netflix-style)
            Surface(
                modifier = Modifier
                    .size(120.dp)
                    .scale(logoScale.value)
                    .alpha(logoAlpha.value),
                shape = CircleShape,
                color = MovieRed,
                shadowElevation = 16.dp
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.White,
                        modifier = Modifier.size(64.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // App Title with gradient
            Text(
                text = "MOVIE APP",
                fontSize = 48.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = 4.sp,
                modifier = Modifier
                    .alpha(textAlpha.value)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Red underline accent
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(4.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MovieRed,
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(2.dp)
                    )
                    .alpha(textAlpha.value)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Tagline
            Text(
                text = "Your Ultimate Movie Experience",
                fontSize = 16.sp,
                fontWeight = FontWeight.Light,
                color = LightGray,
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(textAlpha.value)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle with shimmer
            Text(
                text = "Discover • Watch • Enjoy",
                fontSize = 14.sp,
                color = ElectricBlue.copy(alpha = shimmerAlpha.value),
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center
            )
        }

        // Bottom branding
        Text(
            text = "Powered by TMDB",
            fontSize = 11.sp,
            color = MediumGray,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .alpha(textAlpha.value)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewSplashScreen() {
    SplashScreen()
}