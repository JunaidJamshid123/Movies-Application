package com.example.movieapp.presentation.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.movieapp.presentation.ui.discover.DiscoverScreen
import com.example.movieapp.presentation.ui.home.HomeScreen
import com.example.movieapp.presentation.ui.home.HomeViewModel
import com.example.movieapp.presentation.ui.movies.MovieScreen
import com.example.movieapp.presentation.ui.movies.MovieDetailsScreen
import com.example.movieapp.presentation.ui.movies.MovieDetailsViewModel
import com.example.movieapp.presentation.ui.person.PersonDetailsScreen
import com.example.movieapp.presentation.ui.series.SeriesScreen
import com.example.movieapp.presentation.ui.series.SeriesDetailsScreen
import com.example.movieapp.presentation.ui.favorites.FavoritesScreen
import com.example.movieapp.presentation.ui.movies.MoviesViewModel
import com.example.movieapp.presentation.ui.profile.ProfileScreen
import com.example.movieapp.presentation.ui.series.SeriesViewModel
import com.example.movieapp.presentation.ui.splash.SplashScreen

// Animation specs for smoother transitions
private const val ANIMATION_DURATION = 400
private const val ANIMATION_DURATION_SHORT = 300

private fun enterTransition(): EnterTransition {
    return fadeIn(
        animationSpec = tween(ANIMATION_DURATION, easing = FastOutSlowInEasing)
    ) + slideInHorizontally(
        initialOffsetX = { it / 3 },
        animationSpec = tween(ANIMATION_DURATION, easing = FastOutSlowInEasing)
    )
}

private fun exitTransition(): ExitTransition {
    return fadeOut(
        animationSpec = tween(ANIMATION_DURATION_SHORT, easing = FastOutSlowInEasing)
    ) + slideOutHorizontally(
        targetOffsetX = { -it / 4 },
        animationSpec = tween(ANIMATION_DURATION_SHORT, easing = FastOutSlowInEasing)
    )
}

private fun popEnterTransition(): EnterTransition {
    return fadeIn(
        animationSpec = tween(ANIMATION_DURATION, easing = FastOutSlowInEasing)
    ) + slideInHorizontally(
        initialOffsetX = { -it / 4 },
        animationSpec = tween(ANIMATION_DURATION, easing = FastOutSlowInEasing)
    )
}

private fun popExitTransition(): ExitTransition {
    return fadeOut(
        animationSpec = tween(ANIMATION_DURATION_SHORT, easing = FastOutSlowInEasing)
    ) + slideOutHorizontally(
        targetOffsetX = { it / 3 },
        animationSpec = tween(ANIMATION_DURATION_SHORT, easing = FastOutSlowInEasing)
    )
}

// Detail screen specific transitions (more dramatic)
private fun detailEnterTransition(): EnterTransition {
    return fadeIn(
        animationSpec = tween(ANIMATION_DURATION, easing = FastOutSlowInEasing)
    ) + scaleIn(
        initialScale = 0.92f,
        animationSpec = tween(ANIMATION_DURATION, easing = FastOutSlowInEasing)
    ) + slideInVertically(
        initialOffsetY = { it / 6 },
        animationSpec = tween(ANIMATION_DURATION, easing = FastOutSlowInEasing)
    )
}

private fun detailExitTransition(): ExitTransition {
    return fadeOut(
        animationSpec = tween(ANIMATION_DURATION_SHORT, easing = FastOutSlowInEasing)
    ) + scaleOut(
        targetScale = 1.05f,
        animationSpec = tween(ANIMATION_DURATION_SHORT, easing = FastOutSlowInEasing)
    )
}

private fun detailPopEnterTransition(): EnterTransition {
    return fadeIn(
        animationSpec = tween(ANIMATION_DURATION, easing = FastOutSlowInEasing)
    ) + scaleIn(
        initialScale = 1.05f,
        animationSpec = tween(ANIMATION_DURATION, easing = FastOutSlowInEasing)
    )
}

private fun detailPopExitTransition(): ExitTransition {
    return fadeOut(
        animationSpec = tween(ANIMATION_DURATION_SHORT, easing = FastOutSlowInEasing)
    ) + scaleOut(
        targetScale = 0.92f,
        animationSpec = tween(ANIMATION_DURATION_SHORT, easing = FastOutSlowInEasing)
    ) + slideOutVertically(
        targetOffsetY = { it / 6 },
        animationSpec = tween(ANIMATION_DURATION_SHORT, easing = FastOutSlowInEasing)
    )
}

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        // Splash Screen
        composable(
            route = Screen.Splash.route,
            exitTransition = {
                fadeOut(animationSpec = tween(500)) + scaleOut(
                    targetScale = 1.1f,
                    animationSpec = tween(500)
                )
            }
        ) {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        
        // Home Screen
        composable(
            route = Screen.Home.route,
            enterTransition = { 
                fadeIn(animationSpec = tween(600)) + scaleIn(
                    initialScale = 0.95f,
                    animationSpec = tween(600)
                )
            },
            exitTransition = { exitTransition() },
            popEnterTransition = { popEnterTransition() },
            popExitTransition = { popExitTransition() }
        ) {
            val viewModel: HomeViewModel = hiltViewModel()
            HomeScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        // Movies Screen
        composable(
            route = Screen.Movies.route,
            enterTransition = { enterTransition() },
            exitTransition = { exitTransition() },
            popEnterTransition = { popEnterTransition() },
            popExitTransition = { popExitTransition() }
        ) {
            val viewModel: MoviesViewModel = hiltViewModel()
            MovieScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        // Series Screen
        composable(
            route = Screen.Series.route,
            enterTransition = { enterTransition() },
            exitTransition = { exitTransition() },
            popEnterTransition = { popEnterTransition() },
            popExitTransition = { popExitTransition() }
        ) {
            val viewModel: SeriesViewModel = hiltViewModel()
            SeriesScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        // Favorites Screen
        composable(
            route = Screen.Favorites.route,
            enterTransition = { enterTransition() },
            exitTransition = { exitTransition() },
            popEnterTransition = { popEnterTransition() },
            popExitTransition = { popExitTransition() }
        ) {
            FavoritesScreen(navController = navController)
        }

        // Profile Screen
        composable(
            route = Screen.Profile.route,
            enterTransition = { enterTransition() },
            exitTransition = { exitTransition() },
            popEnterTransition = { popEnterTransition() },
            popExitTransition = { popExitTransition() }
        ) {
            ProfileScreen()
        }

        // Movie Details Screen
        composable(
            route = Screen.MovieDetails.route,
            arguments = listOf(navArgument("movieId") { type = NavType.IntType }),
            enterTransition = { detailEnterTransition() },
            exitTransition = { detailExitTransition() },
            popEnterTransition = { detailPopEnterTransition() },
            popExitTransition = { detailPopExitTransition() }
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
            val viewModel: MovieDetailsViewModel = hiltViewModel()
            MovieDetailsScreen(
                movieId = movieId,
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onPersonClick = { personId ->
                    navController.navigate(Screen.PersonDetails.createRoute(personId))
                },
                onSimilarMovieClick = { similarMovieId ->
                    navController.navigate(Screen.MovieDetails.createRoute(similarMovieId))
                }
            )
        }

        // Series Details Screen
        composable(
            route = Screen.SeriesDetails.route,
            arguments = listOf(navArgument("seriesId") { type = NavType.IntType }),
            enterTransition = { detailEnterTransition() },
            exitTransition = { detailExitTransition() },
            popEnterTransition = { detailPopEnterTransition() },
            popExitTransition = { detailPopExitTransition() }
        ) { backStackEntry ->
            val seriesId = backStackEntry.arguments?.getInt("seriesId") ?: 0
            SeriesDetailsScreen(
                seriesId = seriesId,
                onBackClick = { navController.popBackStack() },
                onPersonClick = { personId ->
                    navController.navigate(Screen.PersonDetails.createRoute(personId))
                },
                onSimilarSeriesClick = { similarSeriesId ->
                    navController.navigate(Screen.SeriesDetails.createRoute(similarSeriesId))
                }
            )
        }

        // Person Details Screen
        composable(
            route = Screen.PersonDetails.route,
            arguments = listOf(navArgument("personId") { type = NavType.IntType }),
            enterTransition = { detailEnterTransition() },
            exitTransition = { detailExitTransition() },
            popEnterTransition = { detailPopEnterTransition() },
            popExitTransition = { detailPopExitTransition() }
        ) { backStackEntry ->
            val personId = backStackEntry.arguments?.getInt("personId") ?: 0
            PersonDetailsScreen(
                personId = personId,
                onBackClick = { navController.popBackStack() },
                onMovieClick = { movieId ->
                    navController.navigate(Screen.MovieDetails.createRoute(movieId))
                },
                onSeriesClick = { seriesId ->
                    navController.navigate(Screen.SeriesDetails.createRoute(seriesId))
                }
            )
        }

        // Discover Screen
        composable(
            route = Screen.Discover.route,
            enterTransition = { enterTransition() },
            exitTransition = { exitTransition() },
            popEnterTransition = { popEnterTransition() },
            popExitTransition = { popExitTransition() }
        ) {
            DiscoverScreen(
                navController = navController
            )
        }
    }
}
