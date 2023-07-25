package net.thebookofcode.www.list_panelattempt.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.NavigationRail
import androidx.compose.material.NavigationRailItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.thebookofcode.www.list_panelattempt.navigation.MenuItem

@Composable
fun DrawerHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 64.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Header", fontSize = 60.sp)
    }
}

@Composable
fun DrawerBody(
    items: List<MenuItem>,
    modifier: Modifier = Modifier,
    itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp),
    onItemClick: (MenuItem) -> Unit
) {
    LazyColumn(modifier) {
        items(items) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onItemClick(item)
                    }
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.description
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = item.title,
                    style = itemTextStyle,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun AppNavRail(
    currentRoute: String,
    navigateToHome: () -> Unit,
    navigateToBookmark: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationRail(
        header = {

        },
        modifier = modifier
    ) {
        Spacer(Modifier.weight(1f))
        NavigationRailItem(
            selected = currentRoute == MenuItem.Home.route,
            onClick = { navigateToHome },
            icon = { Icon(MenuItem.Home.icon, MenuItem.Home.title) },
            label = { Text(text = MenuItem.Home.description) },
            alwaysShowLabel = false
        )
        NavigationRailItem(
            selected = currentRoute == MenuItem.Bookmark.route,
            onClick = { navigateToBookmark },
            icon = { Icon(MenuItem.Bookmark.icon, MenuItem.Bookmark.title) },
            label = { Text(text = MenuItem.Bookmark.description) },
            alwaysShowLabel = false
        )
        NavigationRailItem(
            selected = currentRoute == MenuItem.About.route,
            onClick = { navigateToHome },
            icon = { Icon(MenuItem.About.icon, MenuItem.About.title) },
            label = { Text(text = MenuItem.About.description) },
            alwaysShowLabel = false
        )
        NavigationRailItem(
            selected = currentRoute == MenuItem.Credit.route,
            onClick = { navigateToHome },
            icon = { Icon(MenuItem.Credit.icon, MenuItem.Credit.title) },
            label = { Text(text = MenuItem.Credit.description) },
            alwaysShowLabel = false
        )
        NavigationRailItem(
            selected = currentRoute == MenuItem.RateUs.route,
            onClick = { navigateToHome },
            icon = { Icon(MenuItem.RateUs.icon, MenuItem.RateUs.title) },
            label = { Text(text = MenuItem.RateUs.description) },
            alwaysShowLabel = false
        )
        NavigationRailItem(
            selected = currentRoute == MenuItem.Help.route,
            onClick = { navigateToHome },
            icon = { Icon(MenuItem.Help.icon, MenuItem.Help.title) },
            label = { Text(text = MenuItem.Help.description) },
            alwaysShowLabel = false
        )
        Spacer(Modifier.weight(1f))
    }
}