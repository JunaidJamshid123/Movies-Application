package com.example.movieapp.presentation.ui.home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.Series
import com.example.movieapp.presentation.ui.components.HeroShimmer
import com.example.movieapp.presentation.ui.components.MovieCardShimmer
import com.example.movieapp.presentation.ui.components.ShimmerEffect
import com.example.movieapp.presentation.ui.components.BouncingDotsLoader
import com.example.movieapp.presentation.ui.navigation.Screen
import com.example.movieapp.presentation.ui.theme.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel, navController: NavController) {
    val state = viewModel.uiState.collectAsState().value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        when {
            state.isLoading -> {
                // Enhanced shimmer loading state
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    HeroShimmer()
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Section shimmer
                    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                        ShimmerEffect(
                            modifier = Modifier
                                .width(180.dp)
                                .height(24.dp)
                                .clip(RoundedCornerShape(4.dp))
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        ShimmerEffect(
                            modifier = Modifier
                                .width(120.dp)
                                .height(16.dp)
                                .clip(RoundedCornerShape(4.dp))
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        repeat(4) {
                            MovieCardShimmer()
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Another section
                    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                        ShimmerEffect(
                            modifier = Modifier
                                .width(160.dp)
                                .height(24.dp)
                                .clip(RoundedCornerShape(4.dp))
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        ShimmerEffect(
                            modifier = Modifier
                                .width(100.dp)
                                .height(16.dp)
                                .clip(RoundedCornerShape(4.dp))
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        repeat(4) {
                            MovieCardShimmer()
                        }
                    }
                }
            }
            state.error != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Surface(
                        modifier = Modifier.size(80.dp),
                        shape = CircleShape,
                        color = MovieRed.copy(alpha = 0.1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(20.dp)
                                .size(40.dp),
                            tint = MovieRed
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Oops! Something went wrong",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = state.error,
                        style = MaterialTheme.typography.bodyMedium,
                        color = LightGray,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Hero Section with enhanced carousel
                    if (state.heroItems.isNotEmpty()) {
                        HeroCarousel(
                            movies = state.heroItems,
                            onWatchNowClick = { movieId ->
                                navController.navigate(Screen.MovieDetails.createRoute(movieId))
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Content Sections
                    if (state.trendingMovies.isNotEmpty()) {
                        ContentSection(
                            title = "Trending Movies",
                            subtitle = "What's hot right now"
                        ) {
                            MovieRow(
                                movies = state.trendingMovies,
                                onMovieClick = { movieId ->
                                    navController.navigate(Screen.MovieDetails.createRoute(movieId))
                                },
                                onFavoriteClick = { movie ->
                                    viewModel.toggleMovieFavorite(movie)
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                    }

                    if (state.popularMovies.isNotEmpty()) {
                        ContentSection(
                            title = "Popular Movies",
                            subtitle = "Fan favorites"
                        ) {
                            MovieRow(
                                movies = state.popularMovies,
                                onMovieClick = { movieId ->
                                    navController.navigate(Screen.MovieDetails.createRoute(movieId))
                                },
                                onFavoriteClick = { movie ->
                                    viewModel.toggleMovieFavorite(movie)
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                    }

                    if (state.trendingSeries.isNotEmpty()) {
                        ContentSection(
                            title = "Trending Series",
                            subtitle = "Binge-worthy shows"
                        ) {
                            SeriesRow(
                                series = state.trendingSeries,
                                onSeriesClick = { seriesId ->
                                    navController.navigate(Screen.SeriesDetails.createRoute(seriesId))
                                },
                                onFavoriteClick = { series ->
                                    viewModel.toggleSeriesFavorite(series)
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                    }

                    if (state.popularSeries.isNotEmpty()) {
                        ContentSection(
                            title = "Popular Series",
                            subtitle = "Highly rated shows"
                        ) {
                            SeriesRow(
                                series = state.popularSeries,
                                onSeriesClick = { seriesId ->
                                    navController.navigate(Screen.SeriesDetails.createRoute(seriesId))
                                },
                                onFavoriteClick = { series ->
                                    viewModel.toggleSeriesFavorite(series)
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                    }

                    // Bottom spacing
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

@Composable
fun ContentSection(
    title: String,
    subtitle: String,
    content: @Composable () -> Unit
) {
    Column {
        SectionHeader(title = title, subtitle = subtitle)
        Spacer(modifier = Modifier.height(16.dp))
        content()
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HeroCarousel(movies: List<Movie>, onWatchNowClick: (Int) -> Unit) {
    val pagerState = rememberPagerState(initialPage = 0)
    val coroutineScope = rememberCoroutineScope()

    // Auto-scroll with smoother transition
    LaunchedEffect(pagerState) {
        while (true) {
            delay(6000)
            val nextPage = (pagerState.currentPage + 1) % movies.size
            coroutineScope.launch {
                pagerState.animateScrollToPage(
                    page = nextPage,
                    animationSpec = tween(
                        durationMillis = 800,
                        easing = FastOutSlowInEasing
                    )
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(520.dp)
    ) {
        HorizontalPager(
            count = movies.size,
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
            HeroItem(
                movie = movies[page],
                pageOffset = pageOffset,
                onWatchNowClick = onWatchNowClick
            )
        }

        // Enhanced page indicators
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(movies.size) { index ->
                val isSelected = pagerState.currentPage == index
                val width by animateDpAsState(
                    targetValue = if (isSelected) 24.dp else 8.dp,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
                val color by animateColorAsState(
                    targetValue = if (isSelected) MovieRed else LightGray.copy(alpha = 0.4f),
                    animationSpec = tween(300)
                )

                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .width(width)
                        .height(8.dp)
                        .background(color, shape = RoundedCornerShape(4.dp))
                )
            }
        }
    }
}

@Composable
fun HeroItem(movie: Movie, pageOffset: Float, onWatchNowClick: (Int) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                // Parallax effect
                alpha = 1f - (pageOffset * 0.3f)
                scaleX = 1f - (pageOffset * 0.05f)
                scaleY = 1f - (pageOffset * 0.05f)
            }
    ) {
        // Backdrop Image
        Image(
            painter = rememberAsyncImagePainter(movie.backdropUrl),
            contentDescription = movie.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Enhanced gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.3f),
                            Color.Black.copy(alpha = 0.7f),
                            DarkBackground
                        ),
                        startY = 100f
                    )
                )
        )

        // Content with better spacing
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp,
                    lineHeight = 40.sp
                ),
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Rating
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = GoldenYellow.copy(alpha = 0.15f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = GoldenYellow,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = String.format("%.1f", movie.voteAverage),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Year
                Text(
                    text = movie.releaseDate.take(4),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = LightGray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = movie.overview,
                style = MaterialTheme.typography.bodyMedium.copy(
                    lineHeight = 22.sp
                ),
                color = LightGray.copy(alpha = 0.9f),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Row {
                Button(
                    onClick = { onWatchNowClick(movie.id) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MovieRed,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(52.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Watch Now",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                OutlinedButton(
                    onClick = { /* TODO */ },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.White
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.5.dp,
                        Color.White.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(52.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "My List",
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, subtitle: String) {
    Column(
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            ),
            color = Color.White
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = LightGray.copy(alpha = 0.8f)
        )
    }
}

@Composable
fun MovieRow(movies: List<Movie>, onMovieClick: (Int) -> Unit, onFavoriteClick: (Movie) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(movies) { index, movie ->
            // Staggered animation for each card
            var visible by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) {
                delay(index * 50L)
                visible = true
            }
            
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(
                    animationSpec = tween(400, easing = FastOutSlowInEasing)
                ) + slideInHorizontally(
                    initialOffsetX = { 100 },
                    animationSpec = tween(400, easing = FastOutSlowInEasing)
                )
            ) {
                MovieCard(movie = movie, onMovieClick = onMovieClick, onFavoriteClick = onFavoriteClick)
            }
        }
    }
}

@Composable
fun SeriesRow(series: List<Series>, onSeriesClick: (Int) -> Unit, onFavoriteClick: (Series) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(series) { index, serie ->
            // Staggered animation for each card
            var visible by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) {
                delay(index * 50L)
                visible = true
            }
            
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(
                    animationSpec = tween(400, easing = FastOutSlowInEasing)
                ) + slideInHorizontally(
                    initialOffsetX = { 100 },
                    animationSpec = tween(400, easing = FastOutSlowInEasing)
                )
            ) {
                SeriesCard(series = serie, onSeriesClick = onSeriesClick, onFavoriteClick = onFavoriteClick)
            }
        }
    }
}

@Composable
fun MovieCard(movie: Movie, onMovieClick: (Int) -> Unit, onFavoriteClick: (Movie) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "card_scale"
    )
    
    val elevation by animateDpAsState(
        targetValue = if (isPressed) 12.dp else 8.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "card_elevation"
    )

    Column(
        modifier = Modifier
            .width(150.dp)
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onMovieClick(movie.id)
            }
    ) {
        Box {
            // Poster with enhanced shadow effect
            Surface(
                modifier = Modifier
                    .width(150.dp)
                    .height(225.dp),
                shape = RoundedCornerShape(16.dp),
                shadowElevation = elevation
            ) {
                Box {
                    Image(
                        painter = rememberAsyncImagePainter(movie.posterUrl),
                        contentDescription = movie.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    
                    // Subtle gradient overlay for better text readability
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.3f)
                                    )
                                )
                            )
                    )
                }
            }

            // Rating Badge with glass effect
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp),
                color = Color.Black.copy(alpha = 0.7f),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = GoldenYellow,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = String.format("%.1f", movie.voteAverage),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        ),
                        color = Color.White
                    )
                }
            }
            
            // Animated Favorite Button
            var isFavorited by remember { mutableStateOf(false) }
            val favoriteScale by animateFloatAsState(
                targetValue = if (isFavorited) 1.2f else 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessHigh
                ),
                label = "favorite_scale"
            )
            
            IconButton(
                onClick = { 
                    isFavorited = !isFavorited
                    onFavoriteClick(movie) 
                },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(6.dp)
                    .size(32.dp)
                    .scale(favoriteScale)
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.6f)
                ) {
                    Icon(
                        imageVector = if (isFavorited) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Add to favorites",
                        tint = if (isFavorited) MovieRed else Color.White,
                        modifier = Modifier.padding(6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = movie.title,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                lineHeight = 18.sp
            ),
            color = Color.White,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = movie.releaseDate.take(4),
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 13.sp
            ),
            color = LightGray.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun SeriesCard(series: Series, onSeriesClick: (Int) -> Unit, onFavoriteClick: (Series) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "series_card_scale"
    )
    
    val elevation by animateDpAsState(
        targetValue = if (isPressed) 12.dp else 8.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "series_card_elevation"
    )

    Column(
        modifier = Modifier
            .width(150.dp)
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onSeriesClick(series.id)
            }
    ) {
        Box {
            // Poster with enhanced shadow effect
            Surface(
                modifier = Modifier
                    .width(150.dp)
                    .height(225.dp),
                shape = RoundedCornerShape(16.dp),
                shadowElevation = elevation
            ) {
                Box {
                    Image(
                        painter = rememberAsyncImagePainter(series.posterUrl),
                        contentDescription = series.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    
                    // Subtle gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.3f)
                                    )
                                )
                            )
                    )
                }
            }

            // Rating Badge with glass effect
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp),
                color = Color.Black.copy(alpha = 0.7f),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = GoldenYellow,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = String.format("%.1f", series.voteAverage),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        ),
                        color = Color.White
                    )
                }
            }
            
            // Animated Favorite Button
            var isFavorited by remember { mutableStateOf(false) }
            val favoriteScale by animateFloatAsState(
                targetValue = if (isFavorited) 1.2f else 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessHigh
                ),
                label = "series_favorite_scale"
            )
            
            IconButton(
                onClick = { 
                    isFavorited = !isFavorited
                    onFavoriteClick(series) 
                },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(6.dp)
                    .size(32.dp)
                    .scale(favoriteScale)
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.6f)
                ) {
                    Icon(
                        imageVector = if (isFavorited) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Add to favorites",
                        tint = if (isFavorited) MovieRed else Color.White,
                        modifier = Modifier.padding(6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = series.name,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                lineHeight = 18.sp
            ),
            color = Color.White,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = series.firstAirDate.take(4),
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 13.sp
            ),
            color = LightGray.copy(alpha = 0.7f)
        )
    }
}