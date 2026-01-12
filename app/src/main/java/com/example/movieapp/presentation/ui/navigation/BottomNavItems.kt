package com.example.movieapp.presentation.ui.navigation

import androidx.annotation.DrawableRes
import com.example.movieapp.R

data class BottomNavItem(
    val screen: Screen,
    val title: String,
    @DrawableRes val icon: Int
)

// Example icons (replace with your drawable resources)
val bottomNavItems = listOf(
    BottomNavItem(Screen.Home, "Home", R.drawable.homee),
    BottomNavItem(Screen.Movies, "Movies", R.drawable.movie),
    BottomNavItem(Screen.Series, "Series", R.drawable.tv_series),
    BottomNavItem(Screen.Favorites, "Favorites", R.drawable.fav),
    BottomNavItem(Screen.Profile, "Profile", R.drawable.user)
)
