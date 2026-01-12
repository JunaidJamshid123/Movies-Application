package com.example.movieapp.presentation.ui.movies

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import coil.compose.rememberAsyncImagePainter
import com.example.movieapp.domain.model.Cast
import com.example.movieapp.domain.model.Crew
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.MovieDetails
import com.example.movieapp.presentation.ui.components.CastSection
import com.example.movieapp.presentation.ui.components.CrewSection
import com.example.movieapp.presentation.ui.components.SimilarMoviesSection
import com.example.movieapp.presentation.ui.components.ShimmerEffect
import com.example.movieapp.presentation.ui.components.BouncingDotsLoader
import com.example.movieapp.presentation.ui.theme.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    movieId: Int,
    viewModel: MovieDetailsViewModel,
    onBackClick: () -> Unit,
    onPersonClick: (Int) -> Unit = {},
    onSimilarMovieClick: (Int) -> Unit = {}
) {
    val state = viewModel.uiState.collectAsState().value
    var showTrailerDialog by remember { mutableStateOf(false) }

    LaunchedEffect(movieId) {
        viewModel.loadMovieDetails(movieId)
    }

    // Trailer Dialog
    if (showTrailerDialog && state.trailer != null) {
        com.example.movieapp.presentation.ui.components.YouTubePlayerDialog(
            videoKey = state.trailer.key,
            onDismiss = { showTrailerDialog = false }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        when {
            state.isLoading -> {
                LoadingDetailsState()
            }
            state.error != null -> {
                ErrorDetailsState(error = state.error, onBackClick = onBackClick)
            }
            state.movieDetails != null -> {
                MovieDetailsContent(
                    movieDetails = state.movieDetails,
                    isFavorite = state.isFavorite,
                    trailer = state.trailer,
                    cast = state.cast,
                    directors = state.directors,
                    similarMovies = state.similarMovies,
                    onBackClick = onBackClick,
                    onFavoriteClick = { viewModel.toggleFavorite() },
                    onPlayTrailerClick = { showTrailerDialog = true },
                    onCastClick = onPersonClick,
                    onSimilarMovieClick = onSimilarMovieClick
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsContent(
    movieDetails: MovieDetails,
    isFavorite: Boolean,
    trailer: com.example.movieapp.domain.model.Video?,
    cast: List<Cast>,
    directors: List<Crew>,
    similarMovies: List<Movie>,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onPlayTrailerClick: () -> Unit,
    onCastClick: (Int) -> Unit,
    onSimilarMovieClick: (Int) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header with Back Button and Title
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkBackground)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Text(
                        text = "Movie Details",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        ),
                        color = Color.White
                    )

                    Row {
                        IconButton(
                            onClick = onFavoriteClick,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                                tint = if (isFavorite) MovieRed else Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        
                        IconButton(
                            onClick = { /* Share */ },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

            // Poster Image with rounded corners
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 8.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(480.dp),
                    shape = RoundedCornerShape(20.dp),
                    shadowElevation = 12.dp
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(movieDetails.posterUrl),
                        contentDescription = movieDetails.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Movie Title
            Column(
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Text(
                    text = movieDetails.title,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        color = GoldenYellow
                    ),
                    color = GoldenYellow
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Meta Info Row (Year, Duration, Rating)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Year
                    Text(
                        text = movieDetails.releaseDate.take(4),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        color = LightGray
                    )

                    Text(
                        text = "|",
                        color = LightGray.copy(alpha = 0.5f)
                    )

                    // Duration
                    Text(
                        text = "${movieDetails.runtime / 60}h ${movieDetails.runtime % 60}m",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        color = LightGray
                    )

                    Text(
                        text = "|",
                        color = LightGray.copy(alpha = 0.5f)
                    )

                    // Rating
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = GoldenYellow,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = String.format("%.1f", movieDetails.voteAverage),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Genre Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(movieDetails.genres.take(3)) { genre ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = DarkCard.copy(alpha = 0.6f),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                LightGray.copy(alpha = 0.3f)
                            )
                        ) {
                            Text(
                                text = genre.name,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                ),
                                color = LightGray
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Trailer Section
                if (trailer != null) {
                    com.example.movieapp.presentation.ui.components.TrailerSection(
                        trailer = trailer,
                        onPlayClick = onPlayTrailerClick
                    )
                    Spacer(modifier = Modifier.height(28.dp))
                }

                // Story Line Section
                Text(
                    text = "Story Line",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(12.dp))

                var isExpanded by remember { mutableStateOf(false) }
                val displayText = if (isExpanded || movieDetails.overview.length <= 150) {
                    movieDetails.overview
                } else {
                    movieDetails.overview.take(150) + "..."
                }

                Column {
                    Text(
                        text = displayText,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp,
                            lineHeight = 22.sp
                        ),
                        color = LightGray.copy(alpha = 0.85f)
                    )

                    if (movieDetails.overview.length > 150) {
                        TextButton(
                            onClick = { isExpanded = !isExpanded },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = if (isExpanded) "Show less" else "More...",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = GoldenYellow
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))
            }

            // Cast Section
            if (cast.isNotEmpty()) {
                CastSection(
                    cast = cast,
                    onCastClick = onCastClick
                )
                Spacer(modifier = Modifier.height(28.dp))
            }

            // Directors Section
            if (directors.isNotEmpty()) {
                CrewSection(
                    title = "Directors",
                    crew = directors,
                    onCrewClick = onCastClick
                )
                Spacer(modifier = Modifier.height(28.dp))
            }

            // Similar Movies Section
            if (similarMovies.isNotEmpty()) {
                SimilarMoviesSection(
                    movies = similarMovies,
                    onMovieClick = onSimilarMovieClick
                )
                Spacer(modifier = Modifier.height(28.dp))
            }

            // Additional Details Section
            Column(
                modifier = Modifier.padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                    if (movieDetails.tagline.isNotEmpty()) {
                        DetailInfoRow(
                            label = "Tagline",
                            value = "\"${movieDetails.tagline}\""
                        )
                    }

                    if (movieDetails.budget > 0) {
                        DetailInfoRow(
                            label = "Budget",
                            value = "$${"%,d".format(movieDetails.budget)}"
                        )
                    }

                    if (movieDetails.revenue > 0) {
                        DetailInfoRow(
                            label = "Box Office",
                            value = "$${"%,d".format(movieDetails.revenue)}"
                        )
                    }

                    DetailInfoRow(
                        label = "Release Date",
                        value = movieDetails.releaseDate
                    )

                    DetailInfoRow(
                        label = "Status",
                        value = movieDetails.status
                    )

                    if (movieDetails.imdbId != null) {
                        DetailInfoRow(
                            label = "IMDB ID",
                            value = movieDetails.imdbId
                        )
                    }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun CastMemberCard(
    name: String,
    imageUrl: String?
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Surface(
            modifier = Modifier.size(80.dp),
            shape = CircleShape,
            color = DarkCard,
            shadowElevation = 4.dp
        ) {
            if (imageUrl != null) {
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = name,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    contentScale = ContentScale.Fit
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = LightGray.copy(alpha = 0.5f),
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            ),
            color = LightGray,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun DetailInfoRow(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            ),
            color = LightGray.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            ),
            color = Color.White
        )
    }
}

@Composable
fun LoadingDetailsState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(DarkBackground)
    ) {
        // Header shimmer
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ShimmerEffect(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            ShimmerEffect(
                modifier = Modifier
                    .width(120.dp)
                    .height(24.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ShimmerEffect(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                ShimmerEffect(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
            }
        }
        
        // Poster shimmer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            ShimmerEffect(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(480.dp)
                    .clip(RoundedCornerShape(20.dp))
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Title shimmer
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            ShimmerEffect(
                modifier = Modifier
                    .width(250.dp)
                    .height(32.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.height(12.dp))
            ShimmerEffect(
                modifier = Modifier
                    .width(180.dp)
                    .height(20.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.height(20.dp))
            
            // Genre chips shimmer
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                repeat(3) {
                    ShimmerEffect(
                        modifier = Modifier
                            .width(80.dp)
                            .height(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(28.dp))
            
            // Story line shimmer
            ShimmerEffect(
                modifier = Modifier
                    .width(100.dp)
                    .height(20.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.height(12.dp))
            repeat(3) {
                ShimmerEffect(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                        .clip(RoundedCornerShape(4.dp))
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun ErrorDetailsState(error: String, onBackClick: () -> Unit) {
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
                imageVector = Icons.Default.Close,
                contentDescription = null,
                modifier = Modifier
                    .padding(20.dp)
                    .size(40.dp),
                tint = MovieRed
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Failed to Load Movie",
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
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onBackClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MovieRed
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Go Back", fontWeight = FontWeight.Bold)
        }
    }
}
