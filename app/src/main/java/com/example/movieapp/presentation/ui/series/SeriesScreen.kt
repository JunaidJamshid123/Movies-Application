package com.example.movieapp.presentation.ui.series

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.movieapp.domain.model.Series
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
fun SeriesScreen(viewModel: SeriesViewModel, navController: NavController) {
    val state = viewModel.uiState.collectAsState().value
    var selectedCategory by remember { mutableStateOf("All Series") }
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
                        SeriesScreenTopBar(
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
                            SearchSeriesResultsSection(
                                searchQuery = state.searchQuery,
                                searchResults = state.searchResults,
                                isSearching = state.isSearching,
                                onSeriesClick = { seriesId ->
                                    navController.navigate(Screen.SeriesDetails.createRoute(seriesId))
                                }
                            )
                        }
                    } else {
                        // Hero Section with featured series
                        if (state.popularSeries.isNotEmpty()) {
                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                HeroSeriesCarousel(
                                    series = state.popularSeries.take(5),
                                    onWatchClick = { seriesId ->
                                        navController.navigate(Screen.SeriesDetails.createRoute(seriesId))
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

                        // Popular Series with 3D Carousel Effect
                        if (state.popularSeries.isNotEmpty()) {
                            item {
                                Column {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 24.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Popular",
                                            style = MaterialTheme.typography.titleLarge.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 24.sp
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
                                    PopularSeriesCarousel(series = state.popularSeries, navController = navController)
                                    Spacer(modifier = Modifier.height(24.dp))
                                }
                            }
                        }

                        // Trending Series Grid
                        if (state.trendingSeries.isNotEmpty()) {
                            item {
                                Column(
                                    modifier = Modifier.padding(horizontal = 24.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = "Trending Series",
                                                style = MaterialTheme.typography.titleLarge.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 22.sp
                                                ),
                                                color = Color.White
                                            )
                                            Text(
                                                text = "What's hot right now",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = LightGray.copy(alpha = 0.7f)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }
                        }

                        // Trending Series Grid Items
                        item {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalArrangement = Arrangement.spacedBy(20.dp),
                                modifier = Modifier
                                    .padding(horizontal = 24.dp)
                                    .height((state.trendingSeries.size / 2 * 280).dp),
                                userScrollEnabled = false
                            ) {
                                items(state.trendingSeries) { series ->
                                    TrendingSeriesCard(
                                        series = series,
                                        onSeriesClick = { seriesId ->
                                            navController.navigate(Screen.SeriesDetails.createRoute(seriesId))
                                        }
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
fun SeriesScreenTopBar(
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
                        text = "Series",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp
                        ),
                        color = Color.White
                    )
                    Text(
                        text = "Discover amazing shows",
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
                                "Search series...",
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
fun SearchSeriesResultsSection(
    searchQuery: String,
    searchResults: List<Series>,
    isSearching: Boolean,
    onSeriesClick: (Int) -> Unit
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
                            text = "No series found",
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
                        .height(((searchResults.size + 1) / 2 * 280).dp),
                    userScrollEnabled = false
                ) {
                    items(searchResults) { series ->
                        TrendingSeriesCard(
                            series = series,
                            onSeriesClick = onSeriesClick
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HeroSeriesCarousel(series: List<Series>, onWatchClick: (Int) -> Unit) {
    val pagerState = rememberPagerState(initialPage = 0)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState) {
        while (true) {
            delay(5000)
            val nextPage = (pagerState.currentPage + 1) % series.size
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
            .height(480.dp)
            .padding(horizontal = 24.dp)
    ) {
        HorizontalPager(
            count = series.size,
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
            HeroSeriesCard(
                series = series[page],
                pageOffset = pageOffset,
                onWatchClick = onWatchClick
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(series.size) { index ->
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
fun HeroSeriesCard(series: Series, pageOffset: Float, onWatchClick: (Int) -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                alpha = 1f - (pageOffset * 0.3f)
                scaleX = 1f - (pageOffset * 0.05f)
                scaleY = 1f - (pageOffset * 0.05f)
            },
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 12.dp
    ) {
        Box {
            Image(
                painter = rememberAsyncImagePainter(series.backdropUrl),
                contentDescription = series.name,
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
                                Color.Black.copy(alpha = 0.4f),
                                Color.Black.copy(alpha = 0.9f)
                            ),
                            startY = 100f
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(24.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = GoldenYellow.copy(alpha = 0.15f)
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
                            text = String.format("%.1f", series.voteAverage),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = series.name,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    ),
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = series.overview,
                    style = MaterialTheme.typography.bodyMedium,
                    color = LightGray.copy(alpha = 0.9f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    Button(
                        onClick = { onWatchClick(series.id) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MovieRed
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Watch", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    OutlinedButton(
                        onClick = { /* My List functionality */ },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.5.dp,
                            Color.White.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("My List", fontWeight = FontWeight.Medium)
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
    val categories = listOf("All Series", "Drama", "Comedy", "Action", "Thriller", "Sci-Fi")

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

@Composable
fun PopularSeriesCarousel(series: List<Series>, navController: NavController) {
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = 0)
    val scope = rememberCoroutineScope()

    // Calculate center item based on scroll position
    val centerItemIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val viewportCenter = layoutInfo.viewportStartOffset + (layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset) / 2

            layoutInfo.visibleItemsInfo
                .minByOrNull { kotlin.math.abs((it.offset + it.size / 2) - viewportCenter) }
                ?.index ?: 0
        }
    }

    // Auto-scroll effect
    LaunchedEffect(Unit) {
        delay(1000) // Initial delay
        while (true) {
            delay(3000)
            val nextIndex = (centerItemIndex + 1) % series.size
            scope.launch {
                listState.animateScrollToItem(
                    index = nextIndex,
                    scrollOffset = -100
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        LazyRow(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 100.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(series.size) { index ->
                val isCenter = index == centerItemIndex
                val offset = (index - centerItemIndex).toFloat()

                PopularSeriesCard3D(
                    series = series[index],
                    isCenter = isCenter,
                    offset = offset,
                    onClick = {
                        scope.launch {
                            listState.animateScrollToItem(
                                index = index,
                                scrollOffset = -100
                            )
                        }
                    },
                    onSeriesClick = { seriesId ->
                        navController.navigate(Screen.SeriesDetails.createRoute(seriesId))
                    }
                )
            }
        }
    }
}

@Composable
fun PopularSeriesCard3D(
    series: Series,
    isCenter: Boolean,
    offset: Float,
    onClick: () -> Unit,
    onSeriesClick: (Int) -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isCenter) 1.15f else 0.85f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    val alpha by animateFloatAsState(
        targetValue = if (isCenter) 1f else 0.4f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    val translationY by animateFloatAsState(
        targetValue = if (isCenter) -40f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    Surface(
        modifier = Modifier
            .width(220.dp)
            .height(330.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
                this.translationY = translationY
                rotationY = offset * -12f
            }
            .zIndex(if (isCenter) 2f else 0f)
            .clickable {
                if (isCenter) {
                    onSeriesClick(series.id)
                } else {
                    onClick()
                }
            },
        shape = RoundedCornerShape(20.dp),
        shadowElevation = if (isCenter) 20.dp else 6.dp,
        color = Color(0xFF1A1D29)
    ) {
        Box {
            Image(
                painter = rememberAsyncImagePainter(series.posterUrl),
                contentDescription = series.name,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.8f)
                            ),
                            startY = 150f
                        )
                    )
            )

            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp),
                color = Color.Black.copy(alpha = 0.75f),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = GoldenYellow,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = String.format("%.1f", series.voteAverage),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        ),
                        color = Color.White
                    )
                }
            }

            if (isCenter) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp),
                    color = Color.Black.copy(alpha = 0.75f),
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = Color.White,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(20.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = series.name,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        lineHeight = 20.sp
                    ),
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = series.firstAirDate.take(4),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 13.sp
                    ),
                    color = LightGray.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
fun TrendingSeriesCard(series: Series, onSeriesClick: (Int) -> Unit) {
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
            .fillMaxWidth()
            .scale(scale)
            .clickable { onSeriesClick(series.id) }
    ) {
        Box {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 8.dp
            ) {
                Image(
                    painter = rememberAsyncImagePainter(series.posterUrl),
                    contentDescription = series.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp),
                color = Color.Black.copy(alpha = 0.75f),
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
                fontSize = 12.sp
            ),
            color = LightGray.copy(alpha = 0.7f)
        )
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
                text = "Loading Series...",
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