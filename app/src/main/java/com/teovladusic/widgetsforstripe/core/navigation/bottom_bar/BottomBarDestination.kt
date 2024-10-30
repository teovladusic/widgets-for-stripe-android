package com.teovladusic.widgetsforstripe.core.navigation.bottom_bar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomBarDestination(val route: String) {
    Home("home_screen"), Settings("settings_screen")
}

val BottomBarDestination.icon: ImageVector
    @Composable get() = when (this) {
        BottomBarDestination.Home -> Icons.Default.Home
        BottomBarDestination.Settings -> Icons.Default.Settings
    }
