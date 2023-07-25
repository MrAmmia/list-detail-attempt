package net.thebookofcode.www.list_panelattempt.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import net.thebookofcode.www.list_panelattempt.room.entity.BookmarkWord
import net.thebookofcode.www.list_panelattempt.room.entity.CppWord
import net.thebookofcode.www.list_panelattempt.room.repository.TheBOCRepository
import javax.inject.Inject

@HiltViewModel
class TheBOCViewModel
@Inject constructor(
    private val repository: TheBOCRepository
) : ViewModel() {

    val words:Flow<List<CppWord>> = repository.getAllCppWords()


    fun getAllCppWords(): Flow<List<CppWord>> {
        return repository.getAllCppWords()
    }

    suspend fun getCppWord(key: String): Flow<CppWord> {
        return repository.getCppWord(key)
    }

    suspend fun ifInBookmark(code: Int, key: String): Boolean {
        return repository.ifInBookmark(code, key)
    }

    fun insertBookmark(bookmarkWord: BookmarkWord) = viewModelScope.launch {
        repository.insertBookmark(bookmarkWord)
    }

    fun deleteBookmark(bookmarkWord: BookmarkWord) = viewModelScope.launch {
        repository.deleteBookmark(bookmarkWord)
    }

}