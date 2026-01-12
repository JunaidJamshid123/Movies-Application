package com.example.movieapp.presentation.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.movieapp.presentation.ui.theme.DarkCard
import com.example.movieapp.presentation.ui.theme.DarkGray
import com.example.movieapp.presentation.ui.theme.LightGray

/**
 * Shimmer effect for loading placeholders
 */
@Composable
fun ShimmerEffect(
    modifier: Modifier = Modifier,
    widthOfShadowBrush: Int = 500,
    angleOfAxisY: Float = 270f,
    durationMillis: Int = 1200
) {
    val shimmerColors = listOf(
        DarkCard,
        DarkCard.copy(alpha = 0.3f),
        DarkCard
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = (widthOfShadowBrush + durationMillis).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(x = translateAnimation.value - widthOfShadowBrush, y = 0.0f),
        end = Offset(x = translateAnimation.value, y = angleOfAxisY)
    )

    Box(modifier = modifier.background(brush))
}

/**
 * Shimmer placeholder for movie cards
 */
@Composable
fun MovieCardShimmer(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.width(150.dp)) {
        ShimmerEffect(
            modifier = Modifier
                .width(150.dp)
                .height(225.dp)
                .clip(RoundedCornerShape(16.dp))
        )
        Spacer(modifier = Modifier.height(12.dp))
        ShimmerEffect(
            modifier = Modifier
                .width(120.dp)
                .height(16.dp)
                .clip(RoundedCornerShape(4.dp))
        )
        Spacer(modifier = Modifier.height(6.dp))
        ShimmerEffect(
            modifier = Modifier
                .width(80.dp)
                .height(12.dp)
                .clip(RoundedCornerShape(4.dp))
        )
    }
}

/**
 * Shimmer placeholder for horizontal list
 */
@Composable
fun MovieRowShimmer(
    itemCount: Int = 5,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        repeat(itemCount) {
            MovieCardShimmer()
        }
    }
}

/**
 * Shimmer placeholder for hero section
 */
@Composable
fun HeroShimmer(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.height(500.dp)) {
        ShimmerEffect(
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(24.dp)
        ) {
            ShimmerEffect(
                modifier = Modifier
                    .width(250.dp)
                    .height(36.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.height(12.dp))
            ShimmerEffect(
                modifier = Modifier
                    .width(150.dp)
                    .height(20.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ShimmerEffect(
                    modifier = Modifier
                        .width(120.dp)
                        .height(44.dp)
                        .clip(RoundedCornerShape(22.dp))
                )
                ShimmerEffect(
                    modifier = Modifier
                        .width(120.dp)
                        .height(44.dp)
                        .clip(RoundedCornerShape(22.dp))
                )
            }
        }
    }
}

/**
 * Pressable card with scale animation
 */
@Composable
fun AnimatedPressCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    pressScale: Float = 0.95f,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) pressScale else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "press_scale"
    )

    Box(
        modifier = modifier
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        content()
    }
}

/**
 * Fade-in animation for list items
 */
@Composable
fun AnimatedListItem(
    index: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = 300,
                delayMillis = index * 50,
                easing = FastOutSlowInEasing
            )
        ) + slideInVertically(
            initialOffsetY = { it / 2 },
            animationSpec = tween(
                durationMillis = 300,
                delayMillis = index * 50,
                easing = FastOutSlowInEasing
            )
        ),
        modifier = modifier
    ) {
        content()
    }
}

/**
 * Staggered fade-in for content sections
 */
@Composable
fun StaggeredAnimatedContent(
    delay: Int = 0,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = 400,
                delayMillis = delay,
                easing = FastOutSlowInEasing
            )
        ) + slideInVertically(
            initialOffsetY = { 40 },
            animationSpec = tween(
                durationMillis = 400,
                delayMillis = delay,
                easing = FastOutSlowInEasing
            )
        ),
        modifier = modifier
    ) {
        content()
    }
}

/**
 * Pulsing animation for loading indicators
 */
@Composable
fun PulsingIndicator(
    modifier: Modifier = Modifier,
    color: Color = LightGray
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    Box(
        modifier = modifier
            .scale(scale)
            .graphicsLayer { this.alpha = alpha }
            .background(color, shape = RoundedCornerShape(50))
    )
}

/**
 * Animated counter for numbers
 */
@Composable
fun AnimatedCounter(
    count: Int,
    modifier: Modifier = Modifier,
    style: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyLarge
) {
    var oldCount by remember { mutableIntStateOf(count) }
    
    SideEffect { oldCount = count }
    
    Row(modifier = modifier) {
        val countString = count.toString()
        val oldCountString = oldCount.toString()
        
        for (i in countString.indices) {
            val oldChar = oldCountString.getOrNull(i)
            val newChar = countString[i]
            val char = if (oldChar == newChar) oldCountString[i] else countString[i]
            
            AnimatedContent(
                targetState = char,
                transitionSpec = {
                    slideInVertically { it } togetherWith slideOutVertically { -it }
                },
                label = "counter_$i"
            ) { c ->
                Text(
                    text = c.toString(),
                    style = style,
                    softWrap = false
                )
            }
        }
    }
}

/**
 * Rotating gradient border animation
 */
@Composable
fun AnimatedGradientBorder(
    modifier: Modifier = Modifier,
    colors: List<Color> = listOf(
        Color(0xFFE50914),
        Color(0xFFFFD700),
        Color(0xFF00D9FF),
        Color(0xFFE50914)
    ),
    borderWidth: Dp = 2.dp,
    shape: RoundedCornerShape = RoundedCornerShape(16.dp),
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "gradient_border")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "angle"
    )

    val brush = Brush.sweepGradient(
        colors = colors,
        center = Offset.Unspecified
    )

    Surface(
        modifier = modifier,
        shape = shape,
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer { rotationZ = angle }
                .background(brush, shape)
                .padding(borderWidth)
        ) {
            Surface(
                shape = shape,
                color = DarkCard
            ) {
                content()
            }
        }
    }
}

/**
 * Bouncing dot loading indicator
 */
@Composable
fun BouncingDotsLoader(
    modifier: Modifier = Modifier,
    dotSize: Dp = 10.dp,
    dotColor: Color = Color.White,
    dotCount: Int = 3,
    spaceBetween: Dp = 8.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "bouncing_dots")
    
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spaceBetween),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(dotCount) { index ->
            val offsetY by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = -15f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 400,
                        delayMillis = index * 100,
                        easing = FastOutSlowInEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "dot_$index"
            )
            
            Box(
                modifier = Modifier
                    .size(dotSize)
                    .graphicsLayer { translationY = offsetY }
                    .background(dotColor, RoundedCornerShape(50))
            )
        }
    }
}

/**
 * Expandable content with smooth animation
 */
@Composable
fun ExpandableContent(
    expanded: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = expanded,
        enter = expandVertically(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        ) + fadeIn(
            animationSpec = tween(300)
        ),
        exit = shrinkVertically(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        ) + fadeOut(
            animationSpec = tween(200)
        ),
        modifier = modifier
    ) {
        content()
    }
}

/**
 * Card with hover/focus elevation animation
 */
@Composable
fun AnimatedElevationCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    defaultElevation: Dp = 4.dp,
    pressedElevation: Dp = 8.dp,
    shape: RoundedCornerShape = RoundedCornerShape(16.dp),
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val elevation by animateDpAsState(
        targetValue = if (isPressed) pressedElevation else defaultElevation,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "elevation"
    )
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "scale"
    )

    Surface(
        modifier = modifier
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        shape = shape,
        shadowElevation = elevation,
        color = DarkCard
    ) {
        content()
    }
}
