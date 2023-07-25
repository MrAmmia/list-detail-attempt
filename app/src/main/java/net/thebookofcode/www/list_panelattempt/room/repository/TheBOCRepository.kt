package net.thebookofcode.www.list_panelattempt.room.repository

import kotlinx.coroutines.flow.Flow
import net.thebookofcode.www.list_panelattempt.room.entity.*
import net.thebookofcode.www.list_panelattempt.util.Result

interface TheBOCRepository {
    fun getAllCppWords(): Flow<List<CppWord>>

    fun getAllPythonWords(): Flow<List<PythonWord>>

    fun getAllJavaWords(): Flow<List<JavaWord>>

    suspend fun getCppWord(key: String): Flow<CppWord>

    suspend fun ifInBookmark(code:Int,key:String):Boolean

    suspend fun insertBookmark(bookmarkWord: BookmarkWord)

    suspend fun deleteBookmark(bookmarkWord: BookmarkWord)

    suspend fun getWords(languageCode:Int?): Result<List<Word>>

    suspend fun toggleBookmark(word:Word)
}