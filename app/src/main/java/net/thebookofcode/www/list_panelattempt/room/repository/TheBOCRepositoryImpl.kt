package net.thebookofcode.www.list_panelattempt.room.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import net.thebookofcode.www.list_panelattempt.room.dao.TheBOCDao
import net.thebookofcode.www.list_panelattempt.room.entity.*
import net.thebookofcode.www.list_panelattempt.util.*

class TheBOCRepositoryImpl(private val dao: TheBOCDao) : TheBOCRepository {
    private val words = MutableStateFlow<List<Word>?>(null)

    override fun getAllCppWords(): Flow<List<CppWord>> = dao.getAllCppWords()
    override fun getAllPythonWords(): Flow<List<PythonWord>> = dao.getAllPythonWords()

    override fun getAllJavaWords(): Flow<List<JavaWord>> = dao.getAllJavaWords()

    override suspend fun getCppWord(key: String): Flow<CppWord> = dao.getCppWord(key = key)

    override suspend fun ifInBookmark(code: Int, key: String): Boolean {
        return dao.ifInBookmark(code, key)
    }

    override suspend fun insertBookmark(bookmarkWord: BookmarkWord) {
        dao.insertBookmark(bookmarkWord)
    }

    override suspend fun deleteBookmark(bookmarkWord: BookmarkWord) {
        dao.deleteBookmark(bookmarkWord)
    }

    override suspend fun getWords(languageCode: Int?): Result<List<Word>> {
        return withContext(Dispatchers.IO) {
            try {
                val wordsFromDB = when (languageCode) {
                    1 -> {
                        dao.getAllCppWords().map {
                            cppListToWordList(it)
                        }
                    }
                    2 -> {
                        dao.getAllPythonWords().map {
                            pythonListToWordList(it)
                        }
                    }
                    3 -> {
                        dao.getAllJavaWords().map {
                            javaListToWordList(it)
                        }
                    }
                    else -> {
                        dao.getAllCppWords().map {
                            cppListToWordList(it)
                        }
                    }
                }
                words.update { wordsFromDB.first() }
                Result.Success(wordsFromDB.first())
            } catch (e: Exception) {
                Result.Error(e)
            }

        }
    }

    override suspend fun toggleBookmark(word: Word) {
        if (dao.ifInBookmark(word.code, word.key)) {
            dao.deleteBookmark(
                wordToBookmark(word)
            )
        } else {
            dao.insertBookmark(wordToBookmark(word))
        }
    }

}