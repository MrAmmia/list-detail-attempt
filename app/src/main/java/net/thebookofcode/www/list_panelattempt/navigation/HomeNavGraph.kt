package net.thebookofcode.www.list_panelattempt.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.coroutines.CoroutineScope

import net.thebookofcode.www.list_panelattempt.model.TheBOCViewModel
import net.thebookofcode.www.list_panelattempt.ui.screens.DetailsScreen
import net.thebookofcode.www.list_panelattempt.ui.screens.home.HomeScreen
import net.thebookofcode.www.list_panelattempt.ui.screens.home.HomeViewModel
import net.thebookofcode.www.list_panelattempt.util.*

@Composable
fun HomeNavGraph(
    navController: NavHostController,
    viewModel: TheBOCViewModel,
    homeViewModel: HomeViewModel,
    coroutineScope: CoroutineScope,
    isExpanded: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = HOME_ROUTE,
        route = ROOT_ROUTE
    ) {
        homeGraph(
            navController = navController,
            viewModel = viewModel,
            homeViewModel = homeViewModel,
            isExpanded = isExpanded
        )
    }
}


fun NavGraphBuilder.homeGraph(
    navController: NavHostController,
    viewModel: TheBOCViewModel,
    homeViewModel: HomeViewModel,
    isExpanded: Boolean
) {
    navigation(
        startDestination = DICTIONARY_ROUTE,
        route = HOME_ROUTE
    ) {
        composable(route = DICTIONARY_ROUTE) {
            HomeScreen(viewModel = homeViewModel, isExpanded = isExpanded)
        }
        composable(
            route = "$DETAIL_ROUTE/{$DETAIL_ARGUMENT_KEY}/{$DETAIL_ARGUMENT_KEY2}",
            arguments = listOf(
                navArgument(DETAIL_ARGUMENT_KEY) {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument(DETAIL_ARGUMENT_KEY2) {
                    type = NavType.IntType
                    // nullable = true
                    defaultValue = 0
                }
            )
        ) {
            DetailsScreen(
                key = it.arguments?.getString(DETAIL_ARGUMENT_KEY),
                code = it.arguments?.getInt(DETAIL_ARGUMENT_KEY2)!!,
                viewModel = viewModel,
                modifier = Modifier
            )
        }
    }
}