package net.thebookofcode.www.list_panelattempt.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.ModalDrawer
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import net.thebookofcode.www.list_panelattempt.model.TheBOCViewModel
import net.thebookofcode.www.list_panelattempt.navigation.HomeNavGraph
import net.thebookofcode.www.list_panelattempt.navigation.MenuItem
import net.thebookofcode.www.list_panelattempt.ui.screens.home.HomeViewModel
import net.thebookofcode.www.list_panelattempt.ui.theme.ListPanelAttemptTheme
import net.thebookofcode.www.list_panelattempt.util.HOME_ROUTE


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TheBOCApp(
    widthSizeClass: WindowWidthSizeClass,
    viewModel: TheBOCViewModel,
    homeViewModel: HomeViewModel
) {
    ListPanelAttemptTheme {
        val navController = rememberNavController()
        val coroutineScope = rememberCoroutineScope()

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val isDrawerActive = widthSizeClass == WindowWidthSizeClass.Compact
        val isExpanded = widthSizeClass == WindowWidthSizeClass.Expanded
        val currentRoute =
            navBackStackEntry?.destination?.route ?: HOME_ROUTE

        val sizeAwareDrawerState = rememberSizeAwareDrawerState(isExpanded)
        ModalDrawer(
            gesturesEnabled = !isExpanded,
            drawerContent = {
                DrawerBody(items = listOf(
                    MenuItem.Home,
                    MenuItem.Bookmark,
                    MenuItem.About,
                    MenuItem.Credit,
                    MenuItem.RateUs,
                    MenuItem.Help
                ), onItemClick = { menuItem ->
                    when (menuItem.id) {
                        "help" -> TODO()

                        "rate_us" -> TODO()

                        else -> {
                            navController.navigate(menuItem.route) {
                                popUpTo(navController.graph.findStartDestination().id)
                                launchSingleTop = true
                            }
                        }
                    }
                })
            }
        ) {
            val showNavRail = !isDrawerActive

            Row {
                if (isExpanded) {
                    AppNavRail(
                        currentRoute = MenuItem.Home.route,
                        navigateToHome = {
                            navController.navigate(MenuItem.Home.route)
                        },
                        navigateToBookmark = { MenuItem.Bookmark.route })
                }
                HomeNavGraph(
                    navController = navController,
                    viewModel = viewModel,
                    homeViewModel = homeViewModel,
                    coroutineScope = coroutineScope,
                    isExpanded = isExpanded
                )

                /*HomeScreenWithDetailScreen(
                    viewModel = viewModel,
                    navController = navController
                )
                ListWithDetailsScreen(
                    navController = navController,
                    words = cppListToWordList(cppWords),
                    key = null,
                    code = 0,
                    modifier = Modifier,
                    getWord = { key ->
                        viewModel.getCppWord(key)
                    },
                    checkIfBookmarked = {key,code ->
                        viewModel.ifInBookmark(code,key) },
                    addToBookmark = { word ->
                        viewModel.insertBookmark(wordToBookmark(word)) },
                    removeFromBookmark = {word ->
                        viewModel.deleteBookmark(wordToBookmark(word)) }
                )*/
            }
        }

    }

}


/**
 * Determine the drawer state to pass to the modal drawer.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun rememberSizeAwareDrawerState(isExpandedScreen: Boolean): DrawerState {
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    return if (!isExpandedScreen) {
        // If we want to allow showing the drawer, we use a real, remembered drawer
        // state defined above
        drawerState
    } else {
        // If we don't want to allow the drawer to be shown, we provide a drawer state
        // that is locked closed. This is intentionally not remembered, because we
        // don't want to keep track of any changes and always keep it closed
        DrawerState(DrawerValue.Closed)
    }
}

/**
 * Determine the content padding to apply to the different screens of the app
 */
@Composable
fun rememberContentPaddingForScreen(
    additionalTop: Dp = 0.dp,
    excludeTop: Boolean = false
) =
    WindowInsets.systemBars
        .only(if (excludeTop) WindowInsetsSides.Bottom else WindowInsetsSides.Vertical)
        .add(WindowInsets(top = additionalTop))
        .asPaddingValues()


