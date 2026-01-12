package com.example.movieapp.presentation.ui.discover

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.Series
import com.example.movieapp.presentation.ui.components.ShimmerEffect
import com.example.movieapp.presentation.ui.components.MovieCardShimmer
import com.example.movieapp.presentation.ui.navigation.Screen
import com.example.movieapp.presentation.ui.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen(
    navController: NavController,
    viewModel: DiscoverViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var showFiltersSheet by remember { mutableStateOf(false) }
    var filterOptions by remember { mutableStateOf(FilterOptions()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header with animation
            var headerVisible by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) { headerVisible = true }
            
            AnimatedVisibility(
                visible = headerVisible,
                enter = fadeIn(tween(400)) + slideInVertically(
                    initialOffsetY = { -it },
                    animationSpec = tween(400)
                )
            ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Discover",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )
                
                // Animated filter button
                val filterIconScale by animateFloatAsState(
                    targetValue = if (state.activeFilters != FilterOptions()) 1.1f else 1f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                    label = "filter_scale"
                )
                
                IconButton(
                    onClick = { showFiltersSheet = true },
                    modifier = Modifier.scale(filterIconScale)
                ) {
                    Badge(
                        containerColor = if (state.activeFilters != FilterOptions()) MovieRed else Color.Transparent
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Filters",
                            tint = Color.White
                        )
                    }
                }
            }
            }

            // Content Type Toggle with animation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ContentTypeChip(
                    label = "Movies",
                    selected = state.contentType == ContentType.MOVIES,
                    onClick = { viewModel.setContentType(ContentType.MOVIES) }
                )
                ContentTypeChip(
                    label = "TV Series",
                    selected = state.contentType == ContentType.SERIES,
                    onClick = { viewModel.setContentType(ContentType.SERIES) }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Mood Section
            Text(
                text = "Discover by Mood",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(MoodCategory.values().toList()) { mood ->
                    MoodCard(
                        mood = mood,
                        isSelected = state.activeMood == mood,
                        onClick = { viewModel.selectMood(mood) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Active Filters Display
            if (state.activeMood != null || state.activeFilters != FilterOptions()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = state.activeMood?.displayName ?: "Filtered Results",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )
                    TextButton(onClick = { viewModel.clearFilters() }) {
                        Text("Clear", color = MovieRed)
                    }
                }
            } else {
                Text(
                    text = "Popular ${if (state.contentType == ContentType.MOVIES) "Movies" else "Series"}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Results
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = GoldenYellow)
                }
            } else {
                when (state.contentType) {
                    ContentType.MOVIES -> {
                        if (state.movies.isEmpty()) {
                            EmptyResultsMessage()
                        } else {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                contentPadding = PaddingValues(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                itemsIndexed(state.movies) { index, movie ->
                                    DiscoverMovieCard(
                                        movie = movie,
                                        index = index,
                                        onClick = {
                                            navController.navigate(Screen.MovieDetails.createRoute(movie.id))
                                        }
                                    )
                                }
                            }
                        }
                    }
                    ContentType.SERIES -> {
                        if (state.series.isEmpty()) {
                            EmptyResultsMessage()
                        } else {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                contentPadding = PaddingValues(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                itemsIndexed(state.series) { index, series ->
                                    DiscoverSeriesCard(
                                        series = series,
                                        index = index,
                                        onClick = {
                                            navController.navigate(Screen.SeriesDetails.createRoute(series.id))
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Filters Bottom Sheet
        if (showFiltersSheet) {
            FiltersBottomSheet(
                contentType = state.contentType,
                currentFilters = state.activeFilters,
                onDismiss = { showFiltersSheet = false },
                onApply = { filters ->
                    viewModel.applyFilters(filters)
                    showFiltersSheet = false
                }
            )
        }
    }
}

@Composable
fun ContentTypeChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (selected) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "chip_scale"
    )
    
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) MovieRed else DarkCard,
        animationSpec = tween(300),
        label = "chip_bg_color"
    )
    
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = backgroundColor,
        modifier = Modifier
            .height(36.dp)
            .scale(scale)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
            ),
            color = Color.White
        )
    }
}

@Composable
fun MoodCard(
    mood: MoodCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "mood_scale"
    )
    
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) MovieRed.copy(alpha = 0.3f) else DarkCard,
        animationSpec = tween(300),
        label = "mood_bg"
    )
    
    val emojiScale by animateFloatAsState(
        targetValue = if (isSelected) 1.2f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "emoji_scale"
    )
    
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, MovieRed) else null,
        modifier = Modifier
            .size(width = 120.dp, height = 100.dp)
            .scale(scale)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = mood.emoji,
                fontSize = 28.sp,
                modifier = Modifier.scale(emojiScale)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = mood.displayName,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Color.White,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
            Text(
                text = mood.description,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 10.sp
                ),
                color = LightGray.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun DiscoverMovieCard(
    movie: Movie,
    onClick: () -> Unit,
    index: Int = 0
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "discover_movie_scale"
    )
    
    val elevation by animateDpAsState(
        targetValue = if (isPressed) 8.dp else 4.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "discover_movie_elevation"
    )
    
    // Staggered entry animation
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(index * 50L)
        visible = true
    }
    
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(400)) + scaleIn(
            initialScale = 0.8f,
            animationSpec = tween(400)
        )
    ) {
    Column(
        modifier = Modifier
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.67f),
            shape = RoundedCornerShape(12.dp),
            shadowElevation = elevation
        ) {
            Box {
                Image(
                    painter = rememberAsyncImagePainter(movie.posterUrl),
                    contentDescription = movie.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                // Gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.4f)
                                )
                            )
                        )
                )
                
                // Rating
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    shape = RoundedCornerShape(6.dp),
                    color = Color.Black.copy(alpha = 0.7f)
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
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = String.format("%.1f", movie.voteAverage),
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            ),
                            color = Color.White
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = movie.title,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = Color.White,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        
        Text(
            text = movie.releaseDate.take(4),
            style = MaterialTheme.typography.bodySmall,
            color = LightGray.copy(alpha = 0.7f)
        )
    }
    }
}

@Composable
fun DiscoverSeriesCard(
    series: Series,
    onClick: () -> Unit,
    index: Int = 0
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "discover_series_scale"
    )
    
    val elevation by animateDpAsState(
        targetValue = if (isPressed) 8.dp else 4.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "discover_series_elevation"
    )
    
    // Staggered entry animation
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(index * 50L)
        visible = true
    }
    
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(400)) + scaleIn(
            initialScale = 0.8f,
            animationSpec = tween(400)
        )
    ) {
    Column(
        modifier = Modifier
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.67f),
            shape = RoundedCornerShape(12.dp),
            shadowElevation = elevation
        ) {
            Box {
                Image(
                    painter = rememberAsyncImagePainter(series.posterUrl),
                    contentDescription = series.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                // Gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.4f)
                                )
                            )
                        )
                )
                
                // Rating
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    shape = RoundedCornerShape(6.dp),
                    color = Color.Black.copy(alpha = 0.7f)
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
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = String.format("%.1f", series.voteAverage),
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            ),
                            color = Color.White
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = series.name,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = Color.White,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        
        Text(
            text = series.firstAirDate.take(4),
            style = MaterialTheme.typography.bodySmall,
            color = LightGray.copy(alpha = 0.7f)
        )
    }
    }
}

@Composable
fun EmptyResultsMessage() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = LightGray,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No results found",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Text(
                text = "Try adjusting your filters",
                style = MaterialTheme.typography.bodyMedium,
                color = LightGray
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersBottomSheet(
    contentType: ContentType,
    currentFilters: FilterOptions,
    onDismiss: () -> Unit,
    onApply: (FilterOptions) -> Unit
) {
    var yearFrom by remember { mutableStateOf(currentFilters.yearFrom?.toString() ?: "") }
    var yearTo by remember { mutableStateOf(currentFilters.yearTo?.toString() ?: "") }
    var ratingMin by remember { mutableStateOf(currentFilters.ratingMin ?: 0f) }
    var selectedGenres by remember { mutableStateOf(currentFilters.selectedGenres) }
    var selectedLanguage by remember { mutableStateOf(currentFilters.language) }
    var selectedSort by remember { mutableStateOf(currentFilters.sortBy) }

    val genres = if (contentType == ContentType.MOVIES) GenreData.movieGenres else GenreData.seriesGenres

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = DarkCard
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Filters",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Year Range
            Text(
                text = "Year Range",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = yearFrom,
                    onValueChange = { yearFrom = it.filter { c -> c.isDigit() }.take(4) },
                    label = { Text("From") },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = MovieRed,
                        unfocusedBorderColor = LightGray.copy(alpha = 0.3f)
                    )
                )
                OutlinedTextField(
                    value = yearTo,
                    onValueChange = { yearTo = it.filter { c -> c.isDigit() }.take(4) },
                    label = { Text("To") },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = MovieRed,
                        unfocusedBorderColor = LightGray.copy(alpha = 0.3f)
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Minimum Rating
            Text(
                text = "Minimum Rating: ${String.format("%.1f", ratingMin)}",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Slider(
                value = ratingMin,
                onValueChange = { ratingMin = it },
                valueRange = 0f..10f,
                steps = 19,
                colors = SliderDefaults.colors(
                    thumbColor = MovieRed,
                    activeTrackColor = MovieRed
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Genres
            Text(
                text = "Genres",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                genres.forEach { (id, name) ->
                    FilterChip(
                        selected = selectedGenres.contains(id),
                        onClick = {
                            selectedGenres = if (selectedGenres.contains(id)) {
                                selectedGenres - id
                            } else {
                                selectedGenres + id
                            }
                        },
                        label = { Text(name) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MovieRed,
                            containerColor = DarkCard
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Language
            Text(
                text = "Language",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedLanguage == null,
                    onClick = { selectedLanguage = null },
                    label = { Text("Any") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MovieRed,
                        containerColor = DarkCard
                    )
                )
                GenreData.languages.forEach { (code, name) ->
                    FilterChip(
                        selected = selectedLanguage == code,
                        onClick = { selectedLanguage = code },
                        label = { Text(name) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MovieRed,
                            containerColor = DarkCard
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sort By
            Text(
                text = "Sort By",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GenreData.sortOptions.forEach { (value, label) ->
                    FilterChip(
                        selected = selectedSort == value,
                        onClick = { selectedSort = value },
                        label = { Text(label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MovieRed,
                            containerColor = DarkCard
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        yearFrom = ""
                        yearTo = ""
                        ratingMin = 0f
                        selectedGenres = emptyList()
                        selectedLanguage = null
                        selectedSort = "popularity.desc"
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.White
                    )
                ) {
                    Text("Reset")
                }
                Button(
                    onClick = {
                        onApply(
                            FilterOptions(
                                yearFrom = yearFrom.toIntOrNull(),
                                yearTo = yearTo.toIntOrNull(),
                                ratingMin = if (ratingMin > 0) ratingMin else null,
                                selectedGenres = selectedGenres,
                                language = selectedLanguage,
                                sortBy = selectedSort
                            )
                        )
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MovieRed
                    )
                ) {
                    Text("Apply")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement,
        content = { content() }
    )
}
