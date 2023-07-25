package net.thebookofcode.www.list_panelattempt.ui.screens

import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.IconToggleButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.thebookofcode.www.list_panelattempt.R
import net.thebookofcode.www.list_panelattempt.ui.theme.Typography
import net.thebookofcode.www.list_panelattempt.model.TheBOCViewModel
import net.thebookofcode.www.list_panelattempt.room.entity.Word
import net.thebookofcode.www.list_panelattempt.ui.screens.details.KeySection
import net.thebookofcode.www.list_panelattempt.ui.screens.details.SyntaxSection
import net.thebookofcode.www.list_panelattempt.ui.screens.details.ValueSection
import net.thebookofcode.www.list_panelattempt.util.WordLoadState
import net.thebookofcode.www.list_panelattempt.util.cppToWord
import net.thebookofcode.www.list_panelattempt.util.wordToBookmark

@Composable
fun DetailsScreen(
    key: String?,
    code:Int,
    viewModel: TheBOCViewModel,
    modifier: Modifier,
    /*getWord: suspend (String) -> Flow<CppWord>,
    checkIfBookmarked: suspend (String,Int) -> Boolean,
    addToBookmark: suspend (Word) -> Unit,
    removeFromBookmark: suspend (Word) -> Unit*/
) {
    var loadState by remember { mutableStateOf(WordLoadState.NotSelected) }
    var isBookmarked by remember { mutableStateOf(false) }
    var word by remember { mutableStateOf<Word?>(null) }

    LaunchedEffect(key1 = key, key2 = code){
        /*if(key != null && code != 0){
            loadState = WordLoadState.Loading
            getWord(key).onEach {
                word = cppToWord(it)
            }.launchIn(this)
            isBookmarked = checkIfBookmarked(key,code)
            loadState = WordLoadState.Loaded
        }*/
        if(key != null && code != 0){
            loadState = WordLoadState.Loading
            viewModel.getCppWord(key).onEach {
                word = cppToWord(it)
            }.launchIn(this)
            isBookmarked = viewModel.ifInBookmark(code,key)
            loadState = WordLoadState.Loaded
        }
        else{
            loadState = WordLoadState.NotSelected
        }
    }

    DisplayWord(
        loadState = loadState,
        word = word,
        isBookmarked = isBookmarked,
        viewModel = viewModel
    )


}

@Composable
fun DetailsScreen(
    word:Word,
    viewModel:TheBOCViewModel,
    modifier: Modifier,
) {
    val loadState = WordLoadState.Loaded
    val scope = rememberCoroutineScope()
    var isBookmarked by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = word,){
        isBookmarked = viewModel.ifInBookmark(word.code,word.key)
    }

    DisplayWord(
        loadState = loadState,
        word = word,
        isBookmarked = isBookmarked,
        viewModel = viewModel
    )

}

@Composable
fun WordNotAvailable() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Select a word to display", fontSize = Typography.h4.fontSize)
    }
}

@Composable
fun DisplayWord(
    loadState: WordLoadState,
    word: Word?,
    isBookmarked:Boolean?,
    viewModel:TheBOCViewModel
    /*addToBookmark: suspend (Word) -> Unit,
    removeFromBookmark: suspend (Word) -> Unit*/
) {
    val scope = rememberCoroutineScope()

    when(loadState){
        WordLoadState.NotSelected -> WordNotAvailable()
        WordLoadState.Loading -> Text(text = "Loading")
        WordLoadState.Loaded -> word?.let {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                KeySection(
                    key = it.key,

                )

                ValueSection(value = it.value)

                if (it.syntax != null) {
                    SyntaxSection(syntax = it.syntax!!)
                }

            }
        }?: throw IllegalStateException("If loadstate is loaded, word must be non-null")
    }



}

