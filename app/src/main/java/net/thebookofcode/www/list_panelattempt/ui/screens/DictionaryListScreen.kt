package net.thebookofcode.www.list_panelattempt.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import net.thebookofcode.www.list_panelattempt.R
import net.thebookofcode.www.list_panelattempt.room.entity.Word
import kotlin.collections.ArrayList
import net.thebookofcode.www.list_panelattempt.util.DETAIL_ROUTE
import net.thebookofcode.www.list_panelattempt.ui.theme.Typography
import java.util.*

@Composable
fun ListScreen(
    navController: NavHostController,
    words: List<Word>,
    modifier: Modifier
) {
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    Column {
        SearchView(state = textState)
        DictionaryList(navController = navController,words = words, state = textState)
    }

}

@Composable
fun DictionaryList(
    navController: NavHostController,
    words: List<Word>,
    state: MutableState<TextFieldValue>
) {
    var filteredWords: List<Word>
    LazyColumn() {
        val searchedText = state.value.text
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
                onItemClick = {key,code ->
                    navController.navigate(route = "$DETAIL_ROUTE/$key/$code")
                    /*{
                        popUpTo(route = HOME_ROUTE){
                            saveState = true
                        }
                        // launchSingleTop = true
                        restoreState = true
                    }*/
                }
            )
        }
    }

}

@Composable
fun DictionaryListItem(
    word: Word,
    onItemClick: (String,Int) ->Unit
) {
    Row(
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxWidth()
            .clickable { onItemClick(word.key, word.code) }
            .padding(vertical = 20.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = word.key,
            color = Color.Black,
            fontSize = Typography.h6.fontSize,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun SearchView(state: MutableState<TextFieldValue>) {
    val focusRequester = remember { FocusRequester() }
    val focus = remember { mutableStateOf(false) }
    val inputService = LocalTextInputService.current
    val focusManager = LocalFocusManager.current
    TextField(
        value = state.value,
        onValueChange = { value ->
            state.value = value
        },
        placeholder = {
            Text("Search...",color = Color.White)
        },
        modifier = Modifier
            .width(350.dp)
            .focusRequester(focusRequester)
            .onFocusChanged {
                if (focus.value != it.isFocused) {
                    focus.value = it.isFocused
                    if (!it.isFocused) {
                        inputService?.hideSoftwareKeyboard()
                    }
                }
            },
        textStyle = TextStyle(color = Color.White, fontSize = 18.sp),

        leadingIcon = {
            if (focus.value) {
                IconButton(onClick = {
                    focus.value = false
                    focusManager.clearFocus()
                    inputService?.hideSoftwareKeyboard()
                }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp)
                    )
                }
            }

        },
        trailingIcon = {
            if (state.value != TextFieldValue("")) {
                IconButton(
                    onClick = {
                        state.value =
                            TextFieldValue("") // Remove text from TextField when you press the 'X' icon
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp)
                    )
                }
            } else {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(15.dp)
                        .size(24.dp)
                )
            }
        },
        singleLine = true,
        shape = RectangleShape, // The TextFiled has rounded corners top left and right by default
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.White,
            cursorColor = Color.White,
            leadingIconColor = Color.White,
            trailingIconColor = Color.White,
            backgroundColor = colorResource(id = R.color.colorPrimary),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}
