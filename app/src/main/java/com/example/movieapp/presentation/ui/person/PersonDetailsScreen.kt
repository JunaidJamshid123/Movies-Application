package com.example.movieapp.presentation.ui.person

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.movieapp.domain.model.PersonCredit
import com.example.movieapp.domain.model.PersonDetails
import com.example.movieapp.presentation.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonDetailsScreen(
    personId: Int,
    viewModel: PersonDetailsViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onMovieClick: (Int) -> Unit,
    onSeriesClick: (Int) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(personId) {
        viewModel.loadPersonDetails(personId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = GoldenYellow)
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
                    Text(
                        text = "Error loading person details",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = state.error ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = LightGray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onBackClick) {
                        Text("Go Back")
                    }
                }
            }
            state.personDetails != null -> {
                PersonDetailsContent(
                    personDetails = state.personDetails!!,
                    movieCredits = state.movieCredits,
                    tvCredits = state.tvCredits,
                    onBackClick = onBackClick,
                    onMovieClick = onMovieClick,
                    onSeriesClick = onSeriesClick
                )
            }
        }
    }
}

@Composable
fun PersonDetailsContent(
    personDetails: PersonDetails,
    movieCredits: List<PersonCredit>,
    tvCredits: List<PersonCredit>,
    onBackClick: () -> Unit,
    onMovieClick: (Int) -> Unit,
    onSeriesClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Header with Back Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Person Details",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White
            )
        }

        // Profile Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Image
            Surface(
                modifier = Modifier.size(160.dp),
                shape = CircleShape,
                shadowElevation = 8.dp
            ) {
                if (personDetails.profileUrl != null) {
                    Image(
                        painter = rememberAsyncImagePainter(personDetails.profileUrl),
                        contentDescription = personDetails.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(DarkCard),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = LightGray,
                            modifier = Modifier.size(80.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Name
            Text(
                text = personDetails.name,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Known For
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MovieRed.copy(alpha = 0.2f)
            ) {
                Text(
                    text = personDetails.knownFor,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = MovieRed
                )
            }

            // Birth Info
            if (personDetails.birthday != null || personDetails.placeOfBirth != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (personDetails.birthday != null) {
                        Text(
                            text = "Born: ${personDetails.birthday}${if (personDetails.deathday != null) " - Died: ${personDetails.deathday}" else ""}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = LightGray
                        )
                    }
                    if (personDetails.placeOfBirth != null) {
                        Text(
                            text = personDetails.placeOfBirth,
                            style = MaterialTheme.typography.bodySmall,
                            color = LightGray.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // Biography
        if (personDetails.biography.isNotEmpty()) {
            Spacer(modifier = Modifier.height(24.dp))
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Text(
                    text = "Biography",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                var isExpanded by remember { mutableStateOf(false) }
                val displayText = if (isExpanded || personDetails.biography.length <= 300) {
                    personDetails.biography
                } else {
                    personDetails.biography.take(300) + "..."
                }
                
                Text(
                    text = displayText,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        lineHeight = 22.sp
                    ),
                    color = LightGray.copy(alpha = 0.85f)
                )
                
                if (personDetails.biography.length > 300) {
                    TextButton(
                        onClick = { isExpanded = !isExpanded },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = if (isExpanded) "Show less" else "Read more",
                            color = GoldenYellow,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // Movie Credits
        if (movieCredits.isNotEmpty()) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Movies",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(movieCredits) { credit ->
                    CreditCard(
                        credit = credit,
                        onClick = { onMovieClick(credit.id) }
                    )
                }
            }
        }

        // TV Credits
        if (tvCredits.isNotEmpty()) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "TV Shows",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(tvCredits) { credit ->
                    CreditCard(
                        credit = credit,
                        onClick = { onSeriesClick(credit.id) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun CreditCard(
    credit: PersonCredit,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(120.dp)
            .clickable(onClick = onClick)
    ) {
        Surface(
            modifier = Modifier
                .width(120.dp)
                .height(180.dp),
            shape = RoundedCornerShape(12.dp),
            shadowElevation = 4.dp
        ) {
            Box {
                if (credit.posterUrl != null) {
                    Image(
                        painter = rememberAsyncImagePainter(credit.posterUrl),
                        contentDescription = credit.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(DarkCard),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = credit.title.take(1),
                            style = MaterialTheme.typography.headlineLarge,
                            color = LightGray
                        )
                    }
                }

                // Rating badge
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
                            text = String.format("%.1f", credit.voteAverage),
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
            text = credit.title,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
            ),
            color = Color.White,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        if (credit.character != null) {
            Text(
                text = "as ${credit.character}",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 10.sp
                ),
                color = LightGray.copy(alpha = 0.7f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        } else if (credit.job != null) {
            Text(
                text = credit.job,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 10.sp
                ),
                color = LightGray.copy(alpha = 0.7f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (credit.releaseDate.isNotEmpty()) {
            Text(
                text = credit.releaseDate.take(4),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 10.sp
                ),
                color = LightGray.copy(alpha = 0.5f)
            )
        }
    }
}
