package com.example.movieapp.presentation.ui.movies

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
fun MovieScreen(viewModel: MoviesViewModel, navController: NavController) {
    val state = viewModel.uiState.collectAsState().value
    var selectedCategory by remember { mutableStateOf("All Movies") }
    var isSearchActive by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        when {
            state.isLoading -> {
                LoadingState()
            }
            state.error != null && !isSearchActive -> {
                ErrorState(error = state.error)
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Top Bar
                    item {
                        MovieScreenTopBar(
                            searchQuery = state.searchQuery,
                            isSearchActive = isSearchActive,
                            onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
                            onSearchActiveChange = { 
                                isSearchActive = it
                                if (!it) viewModel.clearSearch()
                            }
                        )
                    }

                    // Show search results when searching
                    if (isSearchActive && state.searchQuery.isNotEmpty()) {
                        // Search Results Section
                        item {
                            SearchResultsSection(
                                searchQuery = state.searchQuery,
                                searchResults = state.searchResults,
                                isSearching = state.isSearching,
                                onMovieClick = { movieId ->
                                    navController.navigate(Screen.MovieDetails.createRoute(movieId))
                                }
                            )
                        }
                    } else {
                        // Featured Hero Section
                        if (state.heroItems.isNotEmpty()) {
                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                FeaturedMovieCarousel(
                                    movies = state.heroItems,
                                    onMovieClick = { movieId ->
                                        navController.navigate(Screen.MovieDetails.createRoute(movieId))
                                    }
                                )
                                Spacer(modifier = Modifier.height(32.dp))
                            }
                        }

                        // Category Tabs
                        item {
                            CategoryTabs(
                                selectedCategory = selectedCategory,
                                onCategorySelected = { selectedCategory = it }
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                        }

                        // Trending Movies Section
                        if (state.trendingMovies.isNotEmpty()) {
                            item {
                                MovieSection(
                                    title = "Trending Now",
                                    subtitle = "Hot picks of the week",
                                    movies = state.trendingMovies,
                                    onMovieClick = { movieId ->
                                        navController.navigate(Screen.MovieDetails.createRoute(movieId))
                                    }
                                )
                                Spacer(modifier = Modifier.height(32.dp))
                            }
                        }

                        // Popular Movies Grid
                        if (state.popularMovies.isNotEmpty()) {
                            item {
                                Column(
                                    modifier = Modifier.padding(horizontal = 24.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Popular",
                                            style = MaterialTheme.typography.titleLarge.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 22.sp
                                            ),
                                            color = Color.White
                                        )
                                        TextButton(onClick = { /* TODO */ }) {
                                            Text(
                                                "View all",
                                                color = MovieRed,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }
                        }

                        // Popular Movies Grid Items
                        item {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalArrangement = Arrangement.spacedBy(20.dp),
                                modifier = Modifier
                                    .padding(horizontal = 24.dp)
                                    .height((state.popularMovies.size / 2 * 310).dp),
                                userScrollEnabled = false
                            ) {
                                items(state.popularMovies) { movie ->
                                    PopularMovieCard(
                                        movie = movie,
                                        isFavorite = state.favoriteMovieIds.contains(movie.id),
                                        onMovieClick = { movieId ->
                                            navController.navigate(Screen.MovieDetails.createRoute(movieId))
                                        },
                                        onFavoriteClick = { viewModel.toggleFavorite(movie) }
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    } // End of else block for search
                }
            }
        }
    }
}

@Composable
fun MovieScreenTopBar(
    searchQuery: String,
    isSearchActive: Boolean,
    onSearchQueryChange: (String) -> Unit,
    onSearchActiveChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        if (!isSearchActive) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Movies",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp
                        ),
                        color = Color.White
                    )
                    Text(
                        text = "Discover amazing films",
                        style = MaterialTheme.typography.bodyMedium,
                        color = LightGray.copy(alpha = 0.7f)
                    )
                }

                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.1f),
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { onSearchActiveChange(true) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.White,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        } else {
            // Search Box
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White.copy(alpha = 0.1f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = LightGray,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    TextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChange,
                        modifier = Modifier.weight(1f),
                        placeholder = {
                            Text(
                                "Search movies...",
                                color = LightGray.copy(alpha = 0.6f)
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        singleLine = true
                    )
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear",
                                tint = LightGray
                            )
                        }
                    }
                    IconButton(onClick = { onSearchActiveChange(false) }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Search",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchResultsSection(
    searchQuery: String,
    searchResults: List<Movie>,
    isSearching: Boolean,
    onMovieClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Search Results",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            ),
            color = Color.White
        )
        
        if (searchQuery.isNotEmpty()) {
            Text(
                text = "Showing results for \"$searchQuery\"",
                style = MaterialTheme.typography.bodyMedium,
                color = LightGray.copy(alpha = 0.7f)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        when {
            isSearching -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MovieRed,
                        strokeWidth = 3.dp,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
            searchResults.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = LightGray.copy(alpha = 0.5f),
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No movies found",
                            style = MaterialTheme.typography.bodyLarge,
                            color = LightGray
                        )
                        Text(
                            text = "Try a different search term",
                            style = MaterialTheme.typography.bodySmall,
                            color = LightGray.copy(alpha = 0.6f)
                        )
                    }
                }
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(((searchResults.size + 1) / 2 * 310).dp),
                    userScrollEnabled = false
                ) {
                    items(searchResults) { movie ->
                        PopularMovieCard(
                            movie = movie,
                            onMovieClick = onMovieClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryTabs(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    val categories = listOf("All Movies", "Action", "Comedy", "Drama", "Thriller", "Sci-Fi")

    LazyRow(
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(categories) { category ->
            CategoryChip(
                label = category,
                isSelected = category == selectedCategory,
                onClick = { onCategorySelected(category) }
            )
        }
    }
}

@Composable
fun CategoryChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) MovieRed else Color.White.copy(alpha = 0.1f),
        animationSpec = tween(300)
    )

    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else LightGray,
        animationSpec = tween(300)
    )

    Surface(
        shape = RoundedCornerShape(24.dp),
        color = backgroundColor,
        modifier = Modifier
            .height(40.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                ),
                color = textColor
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun FeaturedMovieCarousel(movies: List<Movie>, onMovieClick: (Int) -> Unit) {
    val pagerState = rememberPagerState(initialPage = 0)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState) {
        while (true) {
            delay(5000)
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
            .height(420.dp)
            .padding(horizontal = 24.dp)
    ) {
        HorizontalPager(
            count = movies.size,
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
            FeaturedMovieCard(
                movie = movies[page],
                pageOffset = pageOffset,
                onMovieClick = onMovieClick
            )
        }

        // Page Indicators
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(movies.size) { index ->
                val isSelected = pagerState.currentPage == index
                val width by animateDpAsState(
                    targetValue = if (isSelected) 28.dp else 8.dp,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
                val color by animateColorAsState(
                    targetValue = if (isSelected) MovieRed else Color.White.copy(alpha = 0.3f),
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
fun FeaturedMovieCard(movie: Movie, pageOffset: Float, onMovieClick: (Int) -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                alpha = 1f - (pageOffset * 0.3f)
                scaleX = 1f - (pageOffset * 0.1f)
                scaleY = 1f - (pageOffset * 0.1f)
            }
            .clickable { onMovieClick(movie.id) },
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 12.dp
    ) {
        Box {
            Image(
                painter = rememberAsyncImagePainter(movie.backdropUrl),
                contentDescription = movie.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.3f),
                                Color.Black.copy(alpha = 0.8f)
                            ),
                            startY = 200f
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(20.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = GoldenYellow.copy(alpha = 0.2f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
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

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    ),
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = movie.releaseDate.take(4),
                    style = MaterialTheme.typography.bodyMedium,
                    color = LightGray.copy(alpha = 0.9f)
                )
            }
        }
    }
}

@Composable
fun MovieSection(
    title: String,
    subtitle: String,
    movies: List<Movie>,
    onMovieClick: (Int) -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    ),
                    color = Color.White
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = LightGray.copy(alpha = 0.7f)
                )
            }

            TextButton(onClick = { /* TODO */ }) {
                Text(
                    "See All",
                    color = MovieRed,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(movies) { movie ->
                MovieCardCompact(movie = movie, onMovieClick = onMovieClick)
            }
        }
    }
}

@Composable
fun MovieCardCompact(movie: Movie, onMovieClick: (Int) -> Unit) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    Column(
        modifier = Modifier
            .width(140.dp)
            .scale(scale)
            .clickable { onMovieClick(movie.id) }
    ) {
        Box {
            Surface(
                modifier = Modifier
                    .width(140.dp)
                    .height(210.dp),
                shape = RoundedCornerShape(12.dp),
                shadowElevation = 6.dp
            ) {
                Image(
                    painter = rememberAsyncImagePainter(movie.posterUrl),
                    contentDescription = movie.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
                color = Color.Black.copy(alpha = 0.7f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = GoldenYellow,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = String.format("%.1f", movie.voteAverage),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = movie.title,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                lineHeight = 16.sp
            ),
            color = Color.White,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = movie.releaseDate.take(4),
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 12.sp
            ),
            color = LightGray.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun PopularMovieCard(
    movie: Movie,
    isFavorite: Boolean = false,
    onMovieClick: (Int) -> Unit,
    onFavoriteClick: () -> Unit = {}
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )
    
    val genres = remember(movie.genreIds) {
        com.example.movieapp.utils.GenreUtils.getMovieGenreNames(movie.genreIds, 2)
    }
    
    // Genre color pairs for visual variety
    val genreColors = listOf(
        Pair(Color(0xFF3A4F7A).copy(alpha = 0.5f), Color(0xFF7FB3FF)),
        Pair(Color(0xFF4A3A5A).copy(alpha = 0.5f), Color(0xFFB77FFF)),
        Pair(Color(0xFF3A5A4A).copy(alpha = 0.5f), Color(0xFF7FFFB3)),
        Pair(Color(0xFF5A4A3A).copy(alpha = 0.5f), Color(0xFFFFB77F))
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable { onMovieClick(movie.id) },
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 8.dp,
        color = Color(0xFF1A1D29)
    ) {
        Column {
            Box {
                Image(
                    painter = rememberAsyncImagePainter(movie.posterUrl),
                    contentDescription = movie.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    contentScale = ContentScale.Crop
                )

                // Rating Badge
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp),
                    color = Color.Black.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(8.dp)
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

                // Favorite Icon
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp)
                        .clickable(
                            interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                            indication = null
                        ) { onFavoriteClick() },
                    color = Color.Black.copy(alpha = 0.7f),
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (isFavorite) "Remove from Favorites" else "Add to Favorites",
                        tint = if (isFavorite) MovieRed else Color.White,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(18.dp)
                    )
                }
            }

            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        lineHeight = 20.sp
                    ),
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    genres.forEachIndexed { index, genre ->
                        val colors = genreColors[index % genreColors.size]
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = colors.first
                        ) {
                            Text(
                                text = genre,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.sp
                                ),
                                color = colors.second
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = movie.releaseDate.take(4),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp
                    ),
                    color = LightGray.copy(alpha = 0.6f)
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
                text = "Loading Movies...",
                style = MaterialTheme.typography.bodyMedium,
                color = LightGray
            )
        }
    }
}

@Composable
fun ErrorState(error: String) {
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
            text = error,
            style = MaterialTheme.typography.bodyMedium,
            color = LightGray,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}