package net.thebookofcode.www.list_panelattempt.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import net.thebookofcode.www.list_panelattempt.room.dao.TheBOCDao
import net.thebookofcode.www.list_panelattempt.room.database.converters.DateConverter
import net.thebookofcode.www.list_panelattempt.room.entity.BookmarkWord
import net.thebookofcode.www.list_panelattempt.room.entity.CppWord
import net.thebookofcode.www.list_panelattempt.room.entity.JavaWord
import net.thebookofcode.www.list_panelattempt.room.entity.PythonWord

@Database(
    entities = [CppWord::class, JavaWord::class, PythonWord::class, BookmarkWord::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class TheBOCDatabase: RoomDatabase()  {
    abstract fun dao(): TheBOCDao
}