package com.example.movieapp.presentation.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.movieapp.R
import com.example.movieapp.presentation.ui.theme.*

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header with gradient
            ProfileHeader()

            Spacer(modifier = Modifier.height(24.dp))

            // User Info Section
            UserInfoSection(
                userName = state.userName,
                userEmail = state.userEmail
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Statistics Section
            StatisticsSection()

            Spacer(modifier = Modifier.height(32.dp))

            // Settings Section
            SettingsSection(
                notificationsEnabled = state.notificationsEnabled,
                autoPlayEnabled = state.autoPlayEnabled,
                darkModeEnabled = state.darkModeEnabled,
                onToggleNotifications = viewModel::toggleNotifications,
                onToggleAutoPlay = viewModel::toggleAutoPlay,
                onToggleDarkMode = viewModel::toggleDarkMode
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Other Options
            OtherOptionsSection()

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun ProfileHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MovieRed.copy(alpha = 0.3f),
                        DarkBackground
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Picture
            Surface(
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                color = DarkCard,
                shadowElevation = 8.dp
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .border(3.dp, MovieRed, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        modifier = Modifier.size(50.dp),
                        tint = LightGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "My Profile",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun UserInfoSection(userName: String, userEmail: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "Account Information",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = DarkCard
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                InfoRow(
                    icon = Icons.Default.Person,
                    label = "Name",
                    value = userName
                )
                
                Divider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = DarkGray
                )
                
                InfoRow(
                    icon = Icons.Default.Email,
                    label = "Email",
                    value = userEmail
                )
            }
        }
    }
}

@Composable
fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MovieRed,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = LightGray
            )
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
        
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = LightGray,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun StatisticsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "Statistics",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCardDrawable(
                iconRes = R.drawable.movie,
                count = "0",
                label = "Movies",
                modifier = Modifier.weight(1f)
            )
            StatCardDrawable(
                iconRes = R.drawable.tv_series,
                count = "0",
                label = "Series",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                icon = Icons.Default.Favorite,
                count = "0",
                label = "Favorites",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun StatCard(
    icon: ImageVector,
    count: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = DarkCard
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MovieRed,
                modifier = Modifier.size(32.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = count,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Text(
                text = label,
                fontSize = 12.sp,
                color = LightGray
            )
        }
    }
}

@Composable
fun StatCardDrawable(
    iconRes: Int,
    count: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = DarkCard
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = MovieRed,
                modifier = Modifier.size(32.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = count,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Text(
                text = label,
                fontSize = 12.sp,
                color = LightGray
            )
        }
    }
}

@Composable
fun SettingsSection(
    notificationsEnabled: Boolean,
    autoPlayEnabled: Boolean,
    darkModeEnabled: Boolean,
    onToggleNotifications: () -> Unit,
    onToggleAutoPlay: () -> Unit,
    onToggleDarkMode: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "Settings",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = DarkCard
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                SettingRow(
                    icon = Icons.Default.Notifications,
                    title = "Notifications",
                    subtitle = "Enable push notifications",
                    isEnabled = notificationsEnabled,
                    onToggle = onToggleNotifications
                )
                
                Divider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = DarkGray
                )
                
                SettingRow(
                    icon = Icons.Default.PlayArrow,
                    title = "Auto Play",
                    subtitle = "Auto play next episode",
                    isEnabled = autoPlayEnabled,
                    onToggle = onToggleAutoPlay
                )
                
                Divider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = DarkGray
                )
                
                SettingRow(
                    icon = Icons.Default.Star,
                    title = "Dark Mode",
                    subtitle = "Always use dark theme",
                    isEnabled = darkModeEnabled,
                    onToggle = onToggleDarkMode
                )
            }
        }
    }
}

@Composable
fun SettingRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isEnabled: Boolean,
    onToggle: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            shape = CircleShape,
            color = MovieRed.copy(alpha = 0.1f),
            modifier = Modifier.size(40.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MovieRed,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = LightGray
            )
        }
        
        Switch(
            checked = isEnabled,
            onCheckedChange = { onToggle() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = MovieRed,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = DarkGray
            )
        )
    }
}

@Composable
fun OtherOptionsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "Other",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = DarkCard
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                OptionRow(
                    icon = Icons.Default.Settings,
                    title = "Help & Support",
                    onClick = { /* TODO */ }
                )
                
                Divider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = DarkGray
                )
                
                OptionRow(
                    icon = Icons.Default.Info,
                    title = "About",
                    onClick = { /* TODO */ }
                )
                
                Divider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = DarkGray
                )
                
                OptionRow(
                    icon = Icons.Default.Lock,
                    title = "Privacy Policy",
                    onClick = { /* TODO */ }
                )
                
                Divider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = DarkGray
                )
                
                OptionRow(
                    icon = Icons.Default.ExitToApp,
                    title = "Logout",
                    titleColor = MovieRed,
                    onClick = { /* TODO */ }
                )
            }
        }
    }
}

@Composable
fun OptionRow(
    icon: ImageVector,
    title: String,
    titleColor: Color = Color.White,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Surface(
            shape = CircleShape,
            color = if (titleColor == MovieRed) MovieRed.copy(alpha = 0.1f) else DarkSurface,
            modifier = Modifier.size(40.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (titleColor == MovieRed) MovieRed else LightGray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = titleColor,
            modifier = Modifier.weight(1f)
        )
        
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = LightGray,
            modifier = Modifier.size(20.dp)
        )
    }
}