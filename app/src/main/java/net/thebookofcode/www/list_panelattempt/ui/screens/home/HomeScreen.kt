package net.thebookofcode.www.list_panelattempt.ui.screens.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.thebookofcode.www.list_panelattempt.room.entity.BookmarkWord
import net.thebookofcode.www.list_panelattempt.room.entity.Word
import net.thebookofcode.www.list_panelattempt.ui.screens.details.ArticleScreen

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    isExpanded: Boolean,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val ifInBookmark:(Word) -> Boolean = {
        viewModel.ifInBookmark(it)
        viewModel.isFavorite!!
    }
    HomeScreen(
        uiState = uiState,
        isExpanded = isExpanded,
        ifInBookmarkWord = { ifInBookmark(it) },
        onToggleBookmarked = { viewModel.toggleBookmark(it) },
        onSelectWord = { viewModel.selectWord(it) },
        onErrorDismiss = { viewModel.errorShown(it) },
        onInteractWithList = { viewModel.interactedWithList() },
        interactedWithWordDetails = { viewModel.interactedWithWordDetails(it) },
        snackbarHostState = snackbarHostState
    )

}

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    isExpanded: Boolean,
    ifInBookmarkWord: (Word) -> Boolean,
    onToggleBookmarked: (Word) -> Unit,
    onSelectWord: (Word) -> Unit,
    onErrorDismiss: (Long) -> Unit,
    onInteractWithList: () -> Unit,
    interactedWithWordDetails: (Word) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    // Construct the lazy list states for the list and the details outside of deciding which one to
    // show. This allows the associated state to survive beyond that decision, and therefore
    // we get to preserve the scroll throughout any changes to the content.
    val homeListLazyListState = rememberLazyListState()
    val articleDetailLazyListStates = when (uiState) {
        is HomeUiState.HasWords -> uiState.words
        is HomeUiState.NoWords -> emptyList()
    }.associate { word ->
        key(word.key) {
            word.key to rememberLazyListState()
        }
    }

    val homeScreenType = getHomeScreenType(isExpanded, uiState)
    when (homeScreenType) {
        HomeScreenType.ListWithWordDetails -> {
            HomeFeedWithArticleDetailsScreen(
                uiState = uiState,
                onToggleBookmark = { onToggleBookmarked(it) },
                onSelectWord = { onSelectWord(it) },
                onRefreshPosts = { /*TODO*/ },
                onErrorDismiss = { onErrorDismiss(it) },
                onInteractWithList = { onInteractWithList() },
                onInteractWithDetail = { interactedWithWordDetails(it) },
                homeListLazyListState = homeListLazyListState,
                articleDetailLazyListStates = articleDetailLazyListStates,
                snackbarHostState = snackbarHostState
            )

        }
        HomeScreenType.List -> {
            HomeFeedScreen(
                uiState = uiState,
                onToggleBookmark = { onToggleBookmarked(it) },
                onSelectWord = { onSelectWord(it) },
                onRefreshPosts = { /*TODO*/ },
                onErrorDismiss = { onErrorDismiss(it) },
                homeListLazyListState = homeListLazyListState,
                snackbarHostState = snackbarHostState
            )

        }
        HomeScreenType.WordDetails -> {
            // Guaranteed by above condition for home screen type
            check(uiState is HomeUiState.HasWords)

            ArticleScreen(
                word = uiState.selectedWord,
                isExpandedScreen = isExpanded,
                onBack = onInteractWithList,
                isBookmarked = ifInBookmarkWord,
                onToggleFavorite = { onToggleBookmarked })


            // If we are just showing the detail, have a back press switch to the list.
            // This doesn't take anything more than notifying that we "interacted with the list"
            // since that is what drives the display of the feed
            BackHandler {
                onInteractWithList()
            }
        }
    }
}

/**
 * A precise enumeration of which type of screen to display at the home route.
 *
 * There are 3 options:
 * - [FeedWithArticleDetails], which displays both a list of all articles and a specific article.
 * - [Feed], which displays just the list of all articles
 * - [ArticleDetails], which displays just a specific article.
 */
private enum class HomeScreenType {
    ListWithWordDetails,
    List,
    WordDetails
}

/**
 * Returns the current [HomeScreenType] to display, based on whether or not the screen is expanded
 * and the [HomeUiState].
 */
@Composable
private fun getHomeScreenType(
    isExpandedScreen: Boolean,
    uiState: HomeUiState
): HomeScreenType = when (isExpandedScreen) {
    false -> {
        when (uiState) {
            is HomeUiState.HasWords -> {
                if (uiState.isDetailsOpen) {
                    HomeScreenType.WordDetails
                } else {
                    HomeScreenType.List
                }
            }
            is HomeUiState.NoWords -> HomeScreenType.List
        }
    }
    true -> HomeScreenType.ListWithWordDetails
}

