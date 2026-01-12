package com.example.movieapp.presentation.ui.favorites

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.ui.res.painterResource
import androidx.annotation.DrawableRes
import com.example.movieapp.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.Series
import com.example.movieapp.presentation.ui.navigation.Screen
import com.example.movieapp.presentation.ui.theme.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FavoritesScreen(
    navController: NavController,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header with toggle
            FavoritesHeader(
                selectedTab = state.selectedTab,
                onTabSelected = { viewModel.selectTab(it) },
                movieCount = state.favoriteMovies.size,
                seriesCount = state.favoriteSeries.size
            )

            // Content
            when {
                state.isLoading -> {
                    LoadingState()
                }
                state.favoriteMovies.isEmpty() && state.favoriteSeries.isEmpty() -> {
                    EmptyFavoritesState()
                }
                else -> {
                    AnimatedContent(
                        targetState = state.selectedTab,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(300)) togetherWith
                                    fadeOut(animationSpec = tween(300))
                        },
                        label = "tab_animation"
                    ) { tab ->
                        when (tab) {
                            FavoriteTab.MOVIES -> {
                                if (state.favoriteMovies.isEmpty()) {
                                    EmptyTabState(
                                        iconRes = R.drawable.movie,
                                        message = "No favorite movies yet",
                                        subtitle = "Start adding movies to your favorites"
                                    )
                                } else {
                                    FavoriteMoviesGrid(
                                        movies = state.favoriteMovies,
                                        onMovieClick = { movieId ->
                                            navController.navigate(Screen.MovieDetails.createRoute(movieId))
                                        },
                                        onRemoveClick = { movieId ->
                                            viewModel.removeMovieFromFavorites(movieId)
                                        }
                                    )
                                }
                            }
                            FavoriteTab.SERIES -> {
                                if (state.favoriteSeries.isEmpty()) {
                                    EmptyTabState(
                                        iconRes = R.drawable.tv_series,
                                        message = "No favorite series yet",
                                        subtitle = "Start adding series to your favorites"
                                    )
                                } else {
                                    FavoriteSeriesGrid(
                                        series = state.favoriteSeries,
                                        onSeriesClick = { seriesId ->
                                            navController.navigate(Screen.SeriesDetails.createRoute(seriesId))
                                        },
                                        onRemoveClick = { seriesId ->
                                            viewModel.removeSeriesFromFavorites(seriesId)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Error Snackbar
        state.error?.let { error ->
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                action = {
                    TextButton(onClick = { viewModel.clearError() }) {
                        Text("Dismiss", color = MovieRed)
                    }
                }
            ) {
                Text(error)
            }
        }
    }
}

@Composable
fun FavoritesHeader(
    selectedTab: FavoriteTab,
    onTabSelected: (FavoriteTab) -> Unit,
    movieCount: Int,
    seriesCount: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkBackground)
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Text(
            text = "My Favorites",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = "Your collection of beloved content",
            fontSize = 14.sp,
            color = LightGray
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Toggle Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(DarkCard, RoundedCornerShape(12.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ToggleButton(
                text = "Movies",
                count = movieCount,
                isSelected = selectedTab == FavoriteTab.MOVIES,
                onClick = { onTabSelected(FavoriteTab.MOVIES) },
                modifier = Modifier.weight(1f)
            )
            ToggleButton(
                text = "Series",
                count = seriesCount,
                isSelected = selectedTab == FavoriteTab.SERIES,
                onClick = { onTabSelected(FavoriteTab.SERIES) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun ToggleButton(
    text: String,
    count: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) MovieRed else Color.Transparent,
        animationSpec = tween(300)
    )
    
    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else LightGray,
        animationSpec = tween(300)
    )

    Box(
        modifier = modifier
            .fillMaxHeight()
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                fontSize = 15.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = textColor
            )
            if (count > 0) {
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    shape = CircleShape,
                    color = if (isSelected) Color.White.copy(alpha = 0.2f) else DarkGray
                ) {
                    Text(
                        text = count.toString(),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                }
            }
        }
    }
}

@Composable
fun FavoriteMoviesGrid(
    movies: List<Movie>,
    onMovieClick: (Int) -> Unit,
    onRemoveClick: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(movies, key = { it.id }) { movie ->
            FavoriteMovieCard(
                movie = movie,
                onClick = { onMovieClick(movie.id) },
                onRemoveClick = { onRemoveClick(movie.id) }
            )
        }
    }
}

@Composable
fun FavoriteSeriesGrid(
    series: List<Series>,
    onSeriesClick: (Int) -> Unit,
    onRemoveClick: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(series, key = { it.id }) { item ->
            FavoriteSeriesCard(
                series = item,
                onClick = { onSeriesClick(item.id) },
                onRemoveClick = { onRemoveClick(item.id) }
            )
        }
    }
}

@Composable
fun FavoriteMovieCard(
    movie: Movie,
    onClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.67f)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
    ) {
        // Poster Image
        Image(
            painter = rememberAsyncImagePainter(movie.posterUrl),
            contentDescription = movie.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradient Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.8f)
                        ),
                        startY = 200f
                    )
                )
        )

        // Remove Button
        IconButton(
            onClick = onRemoveClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .size(32.dp)
                .background(MovieRed, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Remove from favorites",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }

        // Movie Info
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp)
        ) {
            Text(
                text = movie.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = GoldenYellow,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = String.format("%.1f", movie.voteAverage),
                    fontSize = 12.sp,
                    color = LightGray
                )
            }
        }
    }
}

@Composable
fun FavoriteSeriesCard(
    series: Series,
    onClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.67f)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
    ) {
        // Poster Image
        Image(
            painter = rememberAsyncImagePainter(series.posterUrl),
            contentDescription = series.name,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradient Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.8f)
                        ),
                        startY = 200f
                    )
                )
        )

        // Remove Button
        IconButton(
            onClick = onRemoveClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .size(32.dp)
                .background(MovieRed, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Remove from favorites",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }

        // Series Info
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp)
        ) {
            Text(
                text = series.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = GoldenYellow,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = String.format("%.1f", series.voteAverage),
                    fontSize = 12.sp,
                    color = LightGray
                )
            }
        }
    }
}

@Composable
fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                color = MovieRed,
                strokeWidth = 3.dp,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Loading favorites...",
                style = MaterialTheme.typography.bodyMedium,
                color = LightGray
            )
        }
    }
}

@Composable
fun EmptyFavoritesState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Surface(
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                color = DarkCard
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = LightGray
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "No Favorites Yet",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Start exploring and add your favorite\nmovies and series here",
                fontSize = 14.sp,
                color = LightGray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun EmptyTabState(
    @DrawableRes iconRes: Int,
    message: String,
    subtitle: String
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Surface(
                modifier = Modifier.size(80.dp),
                shape = CircleShape,
                color = DarkCard
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = LightGray
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            Text(
                text = message,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Spacer(modifier = Modifier.height(6.dp))
            
            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = LightGray,
                textAlign = TextAlign.Center
            )
        }
    }
}