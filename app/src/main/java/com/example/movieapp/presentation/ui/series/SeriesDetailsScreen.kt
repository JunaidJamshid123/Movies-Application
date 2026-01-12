package com.example.movieapp.presentation.ui.series

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.movieapp.domain.model.Cast
import com.example.movieapp.domain.model.Crew
import com.example.movieapp.domain.model.Series
import com.example.movieapp.domain.model.SeriesDetails
import com.example.movieapp.presentation.ui.components.CastSection
import com.example.movieapp.presentation.ui.components.CrewSection
import com.example.movieapp.presentation.ui.components.SimilarSeriesSection
import com.example.movieapp.presentation.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeriesDetailsScreen(
    seriesId: Int,
    onBackClick: () -> Unit,
    onPersonClick: (Int) -> Unit = {},
    onSimilarSeriesClick: (Int) -> Unit = {}
) {
    val viewModel: SeriesDetailsViewModel = hiltViewModel()
    val state = viewModel.uiState.collectAsState().value

    LaunchedEffect(seriesId) {
        viewModel.loadSeriesDetails(seriesId)
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
            state.seriesDetails != null -> {
                SeriesDetailsContent(
                    seriesDetails = state.seriesDetails,
                    isFavorite = state.isFavorite,
                    trailer = state.trailer,
                    cast = state.cast,
                    creators = state.creators,
                    similarSeries = state.similarSeries,
                    onBackClick = onBackClick,
                    onFavoriteClick = { viewModel.toggleFavorite() },
                    onPersonClick = onPersonClick,
                    onSimilarSeriesClick = onSimilarSeriesClick
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeriesDetailsContent(
    seriesDetails: SeriesDetails,
    isFavorite: Boolean,
    trailer: com.example.movieapp.domain.model.Video?,
    cast: List<Cast>,
    creators: List<Crew>,
    similarSeries: List<Series>,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onPersonClick: (Int) -> Unit,
    onSimilarSeriesClick: (Int) -> Unit
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
                        text = "Series Details",
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
                        painter = rememberAsyncImagePainter(seriesDetails.posterUrl),
                        contentDescription = seriesDetails.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Series Title
            Column(
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Text(
                    text = seriesDetails.name,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        color = GoldenYellow
                    ),
                    color = GoldenYellow
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Meta Info Row (Year, Seasons, Rating)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Year
                    Text(
                        text = seriesDetails.firstAirDate.take(4),
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

                    // Seasons & Episodes
                    Text(
                        text = "${seriesDetails.numberOfSeasons} Season${if (seriesDetails.numberOfSeasons > 1) "s" else ""} · ${seriesDetails.numberOfEpisodes} Episodes",
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
                        text = String.format("%.1f", seriesDetails.voteAverage),
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
                    items(seriesDetails.genres.take(3)) { genre ->
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
                        onPlayClick = { }
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
                val displayText = if (isExpanded || seriesDetails.overview.length <= 150) {
                    seriesDetails.overview
                } else {
                    seriesDetails.overview.take(150) + "..."
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

                    if (seriesDetails.overview.length > 150) {
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
                    onCastClick = onPersonClick
                )
                Spacer(modifier = Modifier.height(28.dp))
            }

            // Creators Section
            if (creators.isNotEmpty()) {
                CrewSection(
                    title = "Creators",
                    crew = creators,
                    onCrewClick = onPersonClick
                )
                Spacer(modifier = Modifier.height(28.dp))
            }

            // Similar Series Section
            if (similarSeries.isNotEmpty()) {
                SimilarSeriesSection(
                    series = similarSeries,
                    onSeriesClick = onSimilarSeriesClick
                )
                Spacer(modifier = Modifier.height(28.dp))
            }

            // Series Information Section
            Column(
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                // Cast Section (placeholder with crew names if available)
                Text(
                    text = "Info",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Series Information
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (seriesDetails.tagline.isNotEmpty()) {
                        DetailInfoRow(
                            label = "Tagline",
                            value = "\"${seriesDetails.tagline}\""
                        )
                    }

                    DetailInfoRow(
                        label = "First Air Date",
                        value = seriesDetails.firstAirDate
                    )

                    DetailInfoRow(
                        label = "Total Seasons",
                        value = "${seriesDetails.numberOfSeasons} Season${if (seriesDetails.numberOfSeasons > 1) "s" else ""}"
                    )

                    DetailInfoRow(
                        label = "Total Episodes",
                        value = "${seriesDetails.numberOfEpisodes} Episodes"
                    )

                    DetailInfoRow(
                        label = "Status",
                        value = seriesDetails.status
                    )

                    DetailInfoRow(
                        label = "Vote Count",
                        value = "${"%,d".format(seriesDetails.voteCount)} votes"
                    )

                    DetailInfoRow(
                        label = "Popularity",
                        value = String.format("%.1f", seriesDetails.popularity)
                    )

                    if (seriesDetails.homepage != null && seriesDetails.homepage.isNotEmpty()) {
                        DetailInfoRow(
                            label = "Homepage",
                            value = "Available"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
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
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                color = GoldenYellow,
                strokeWidth = 3.dp,
                modifier = Modifier.size(56.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Loading Details...",
                style = MaterialTheme.typography.bodyLarge,
                color = LightGray
            )
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
            text = "Failed to Load Series",
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
