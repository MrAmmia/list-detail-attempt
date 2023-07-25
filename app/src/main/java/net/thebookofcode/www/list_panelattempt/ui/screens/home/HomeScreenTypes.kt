package net.thebookofcode.www.list_panelattempt.ui.screens.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive
import net.thebookofcode.www.list_panelattempt.R
import net.thebookofcode.www.list_panelattempt.room.entity.Word
import net.thebookofcode.www.list_panelattempt.ui.components.TheBOCSnackbarHost
import net.thebookofcode.www.list_panelattempt.ui.screens.DictionaryListItem
import net.thebookofcode.www.list_panelattempt.ui.screens.SearchView
import net.thebookofcode.www.list_panelattempt.ui.screens.details.detailContentItems
import net.thebookofcode.www.list_panelattempt.ui.screens.rememberContentPaddingForScreen
import net.thebookofcode.www.list_panelattempt.util.DETAIL_ROUTE
import java.util.*
import kotlin.collections.ArrayList


/**
 * The home screen displaying the feed along with an article details.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeFeedWithArticleDetailsScreen(
    uiState: HomeUiState,
    onToggleBookmark: (Word) -> Unit,
    onSelectWord: (Word) -> Unit,
    onRefreshPosts: () -> Unit,
    onErrorDismiss: (Long) -> Unit,
    onInteractWithList: () -> Unit,
    onInteractWithDetail: (Word) -> Unit,
    homeListLazyListState: LazyListState,
    articleDetailLazyListStates: Map<String, LazyListState>,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    HomeScreenWithList(
        uiState = uiState,
        onRefreshPosts = onRefreshPosts,
        onErrorDismiss = onErrorDismiss,
        snackbarHostState = snackbarHostState,
        modifier = modifier,
    ) { hasPostsUiState, contentModifier ->
        val contentPadding = rememberContentPaddingForScreen(
            additionalTop = 8.dp
        )
        Row(contentModifier) {

            WordList(
                words = hasPostsUiState.words,
                onArticleTapped = { onSelectWord(it) },
                modifier = Modifier.width(350.dp)
            )

            // Crossfade between different detail posts
            Crossfade(targetState = hasPostsUiState.selectedWord) { detailPost ->
                // Get the lazy list state for this detail view
                val detailLazyListState by remember {
                    derivedStateOf {
                        articleDetailLazyListStates.getValue(detailPost.key)
                    }
                }

                // Key against the post id to avoid sharing any state between different posts
                key(detailPost.key) {
                    LazyColumn(
                        state = detailLazyListState,
                        contentPadding = contentPadding,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxSize()
                            .notifyInput {
                                onInteractWithDetail(detailPost)
                            }
                    ) {
                        // details go here
                        detailContentItems(detailPost)
                    }
                }
            }


        }
    }
}


/**
 * The home screen displaying just the article feed.
 */
@Composable
fun HomeFeedScreen(
    uiState: HomeUiState,
    onToggleBookmark: (Word) -> Unit,
    onSelectWord: (Word) -> Unit,
    onRefreshPosts: () -> Unit,
    onErrorDismiss: (Long) -> Unit,
    homeListLazyListState: LazyListState,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    HomeScreenWithList(
        uiState = uiState,
        onRefreshPosts = onRefreshPosts,
        onErrorDismiss = onErrorDismiss,
        snackbarHostState = snackbarHostState,
        modifier = modifier
    ) { hasPostsUiState, contentModifier ->
        WordList(
            words = hasPostsUiState.words,
            onArticleTapped = { onSelectWord(it) },
            modifier = Modifier
        )
    }
}


/**
 * A display of the home screen that has the list.
 *
 * This sets up the scaffold with the top app bar, and surrounds the [hasPostsContent] with refresh,
 * loading and error handling.
 *
 * This helper functions exists because [HomeFeedWithArticleDetailsScreen] and [HomeFeedScreen] are
 * extremely similar, except for the rendered content when there are posts to display.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenWithList(
    uiState: HomeUiState,
    onRefreshPosts: () -> Unit,
    onErrorDismiss: (Long) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    hasPostsContent: @Composable (
        uiState: HomeUiState.HasWords,
        modifier: Modifier
    ) -> Unit
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState)
    Scaffold(
        snackbarHost = { TheBOCSnackbarHost(hostState = snackbarHostState) },
        modifier = modifier
    ) { innerPadding ->
        val contentModifier = Modifier
            .padding(innerPadding)
            .nestedScroll(scrollBehavior.nestedScrollConnection)

        LoadingContent(
            empty = when (uiState) {
                is HomeUiState.HasWords -> false
                is HomeUiState.NoWords -> uiState.isLoading
            },
            emptyContent = { FullScreenLoading() },
            loading = uiState.isLoading,
            onRefresh = onRefreshPosts,
            content = {
                when (uiState) {
                    is HomeUiState.HasWords -> hasPostsContent(uiState, contentModifier)
                    is HomeUiState.NoWords -> {
                        if (uiState.errorMessages.isEmpty()) {
                            // if there are no posts, and no error, let the user refresh manually
                            TextButton(
                                onClick = onRefreshPosts,
                                modifier.fillMaxSize()
                            ) {
                                Text(
                                    stringResource(id = R.string.home_tap_to_load_content),
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            // there's currently an error showing, don't show any content
                            Box(contentModifier.fillMaxSize()) { /* empty screen */ }
                        }
                    }
                }
            }
        )
    }

    // Process one error message at a time and show them as Snackbars in the UI
    if (uiState.errorMessages.isNotEmpty()) {
        // Remember the errorMessage to display on the screen
        val errorMessage = remember(uiState) { uiState.errorMessages[0] }

        // Get the text to show on the message from resources
        val errorMessageText: String = stringResource(errorMessage.messageId)
        val retryMessageText = stringResource(id = R.string.retry)

        // If onRefreshPosts or onErrorDismiss change while the LaunchedEffect is running,
        // don't restart the effect and use the latest lambda values.
        val onRefreshPostsState by rememberUpdatedState(onRefreshPosts)
        val onErrorDismissState by rememberUpdatedState(onErrorDismiss)

        // Effect running in a coroutine that displays the Snackbar on the screen
        // If there's a change to errorMessageText, retryMessageText or snackbarHostState,
        // the previous effect will be cancelled and a new one will start with the new values
        LaunchedEffect(errorMessageText, retryMessageText, snackbarHostState) {
            val snackbarResult = snackbarHostState.showSnackbar(
                message = errorMessageText,
                actionLabel = retryMessageText
            )
            if (snackbarResult == SnackbarResult.ActionPerformed) {
                onRefreshPostsState()
            }
            // Once the message is displayed and dismissed, notify the ViewModel
            onErrorDismissState(errorMessage.id)
        }
    }
}

/**
 * Display an initial empty state or swipe to refresh content.
 *
 * @param empty (state) when true, display [emptyContent]
 * @param emptyContent (slot) the content to display for the empty state
 * @param loading (state) when true, display a loading spinner over [content]
 * @param onRefresh (event) event to request refresh
 * @param content (slot) the main content to show
 */
@Composable
private fun LoadingContent(
    empty: Boolean,
    emptyContent: @Composable () -> Unit,
    loading: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit
) {
    if (empty) {
        emptyContent()
    } else {
        content()

    }
}


/**
 * Full screen circular progress indicator
 */
@Composable
private fun FullScreenLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        CircularProgressIndicator()
    }
}

/**
 * A [Modifier] that tracks all input, and calls [block] every time input is received.
 */
private fun Modifier.notifyInput(block: () -> Unit): Modifier =
    composed {
        val blockState = rememberUpdatedState(block)
        pointerInput(Unit) {
            while (currentCoroutineContext().isActive) {
                awaitPointerEventScope {
                    awaitPointerEvent(PointerEventPass.Initial)
                    blockState.value()
                }
            }
        }
    }

@Composable
fun WordList(
    words: List<Word>,
    onArticleTapped: (word: Word) -> Unit,
    modifier: Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    lazyListState: LazyListState = rememberLazyListState()
) {
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    var filteredWords: List<Word>
    Column {
        SearchView(state = textState)
        LazyColumn(
            modifier = modifier,
            contentPadding = contentPadding,
            state = lazyListState
        )
        {
            val searchedText = textState.value.text
            filteredWords = if (searchedText.isEmpty()) {
                words
            } else {
                val resultList = ArrayList<Word>()
                for (word in words) {
                    if (word.key.lowercase(Locale.getDefault())
                            .contains(searchedText.lowercase(Locale.getDefault()))
                    ) {
                        resultList.add(word)
                    }

                }
                resultList
            }
            items(items = filteredWords) {
                DictionaryListItem(
                    word = it,
                    onItemClick = { key, code ->
                        onArticleTapped(it)
                    }
                )
            }
        }
    }
}
