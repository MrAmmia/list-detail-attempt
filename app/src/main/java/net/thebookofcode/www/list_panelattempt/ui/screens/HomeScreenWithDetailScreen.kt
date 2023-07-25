package net.thebookofcode.www.list_panelattempt.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import net.thebookofcode.www.list_panelattempt.model.TheBOCViewModel
import net.thebookofcode.www.list_panelattempt.room.entity.Word
import net.thebookofcode.www.list_panelattempt.ui.screens.home.HomeUiState
import net.thebookofcode.www.list_panelattempt.ui.screens.home.HomeViewModel
import net.thebookofcode.www.list_panelattempt.util.cppListToWordList




@Composable
fun HomeScreenWithDetailScreen(
    viewModel: TheBOCViewModel,
    navController: NavHostController,
    modifier: Modifier,
    words:List<Word>
) {
    Row {
        /*val cppWords by viewModel.words.collectAsState(initial = emptyList())
        val words = cppListToWordList(cppWords)*/
        ListScreen(navController = navController, words = words, modifier = Modifier.width(334.dp))
        DetailsScreen(
            word = words[0],
            viewModel = viewModel,
            modifier = Modifier.fillMaxWidth()
        )

    }
}

