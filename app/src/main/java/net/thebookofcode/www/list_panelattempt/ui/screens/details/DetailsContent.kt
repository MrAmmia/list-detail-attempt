package net.thebookofcode.www.list_panelattempt.ui.screens.details

import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.IconToggleButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import net.thebookofcode.www.list_panelattempt.R
import net.thebookofcode.www.list_panelattempt.room.entity.Word

private val defaultSpacerSize = 16.dp

@Composable
fun DetailsContent(
    word: Word,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        contentPadding = PaddingValues(defaultSpacerSize),
        modifier = modifier,
        state = state,
    ) {
        detailContentItems(word)
    }
}

fun LazyListScope.detailContentItems(word: Word) {
    item {
        KeySection(key = word.key)
        Spacer(Modifier.height(defaultSpacerSize))
    }


    item {
        Text("Example", style = MaterialTheme.typography.headlineLarge)
        ValueSection(value = word.value)
        Spacer(Modifier.height(defaultSpacerSize))
    }
    item {
        Text("Syntax", style = MaterialTheme.typography.headlineLarge)
        word.syntax?.let { SyntaxSection(syntax = it) }
    }
}

@Composable
fun KeySection(
    key: String
) {
    Text(
        text = key,
        fontSize = 20.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = colorResource(id = R.color.orange),
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
fun ValueSection(value: String) {
    AndroidView(factory = {
        TextView(it)
    }, update = { it.text = HtmlCompat.fromHtml(value, HtmlCompat.FROM_HTML_MODE_LEGACY) })
}

@Composable
fun SyntaxSection(syntax: String) {
    Column {
        Text(
            text = "Example",
            fontSize = 20.sp,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        AndroidView(factory = {
            TextView(it)
        }, update = { it.text = HtmlCompat.fromHtml(syntax, HtmlCompat.FROM_HTML_MODE_LEGACY) })
    }

}

