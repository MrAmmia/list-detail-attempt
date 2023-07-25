package net.thebookofcode.www.list_panelattempt.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import net.thebookofcode.www.list_panelattempt.room.entity.BookmarkWord
import net.thebookofcode.www.list_panelattempt.room.entity.CppWord
import net.thebookofcode.www.list_panelattempt.room.entity.JavaWord
import net.thebookofcode.www.list_panelattempt.room.entity.PythonWord

@Dao
interface TheBOCDao {
    @Query("SELECT * FROM cpp")
    fun getAllCppWords(): Flow<List<CppWord>>

    @Query("SELECT * FROM java")
    fun getAllJavaWords(): Flow<List<JavaWord>>

    @Query("SELECT * FROM python")
    fun getAllPythonWords(): Flow<List<PythonWord>>

    @Query("SELECT * FROM cpp WHERE `key` = :key")
    fun getCppWord(key:String): Flow<CppWord>

    @Query("SELECT * FROM java WHERE `key` = :key")
    fun getJavaWord(key:String): JavaWord

    @Query("SELECT * FROM python WHERE `key` = :key")
    fun getPythonWord(key:String): PythonWord

    @Query("SELECT * FROM bookmark WHERE `key` = :key")
    fun getBookmarkWord(key:String): BookmarkWord

    @Query("SELECT * FROM bookmark ORDER BY date DESC")
    fun getAllBookmarkWords(): Flow<List<BookmarkWord>>

    @Insert
    suspend fun insertBookmark(bookmarkWord: BookmarkWord)

    @Delete
    suspend fun deleteBookmark(bookmarkWord: BookmarkWord)

    @Query("DELETE FROM bookmark")
    suspend fun deleteAllBookmarks()

    @Query("SELECT EXISTS(SELECT * FROM bookmark WHERE code = :code AND `key` = :key)")
    suspend fun ifInBookmark(code :Int, key:String):Boolean
}