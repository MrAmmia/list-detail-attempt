package net.thebookofcode.www.list_panelattempt.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class MenuItem (
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val route: String = ""
){
    object Home : MenuItem(
        id = "home",
        title = "Home",
        description = "Home Screen",
        icon = Icons.Default.Home,
        route = "HOME_SCREEN"
    )

    object Bookmark : MenuItem(
        id = "bookmark",
        title = "Bookmark",
        description = "Bookmark Screen",
        icon = Icons.Default.Person,
        route = "BOOKMARK_SCREEN"
    )

    object About : MenuItem(
        id = "about",
        title = "About",
        description = "About Screen",
        icon = Icons.Default.Settings,
        route = "ABOUT_SCREEN"
    )

    object Credit : MenuItem(
        id = "credit",
        title = "Credit",
        description = "Credit Button",
        icon = Icons.Default.Info,
        route = "CREDIT_SCREEN"
    )

    object Help : MenuItem(
        id = "help",
        title = "Help",
        description = "Help Button",
        icon = Icons.Default.Info
    )

    object RateUs : MenuItem(
        id = "rate_us",
        title = "Rate Us",
        description = "Rate Us",
        icon = Icons.Default.Star
    )
}
