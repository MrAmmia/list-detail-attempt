package net.thebookofcode.www.list_panelattempt.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.thebookofcode.www.list_panelattempt.room.dao.TheBOCDao
import net.thebookofcode.www.list_panelattempt.room.database.TheBOCDatabase
import net.thebookofcode.www.list_panelattempt.util.DATABASE_NAME
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Volatile
    private var INSTANCE: TheBOCDatabase? = null

    private class TheBOCDatabaseCallback(private val scope: CoroutineScope) :
        RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val dao = database.dao()

                    // Delete all content here.
                    //dao.deleteAllNotes()

                    // Add sample words.
                    /*
                    dao.getAllCppWords()
                    dao.getAllJavaWords()
                    dao.getAllPythonWords()
                    */
                }
            }
        }
    }

    @Singleton
    @Provides
    fun provideTheBOCDB(@ApplicationContext context: Context): TheBOCDatabase {
        return INSTANCE ?: synchronized(this) {
            val scope = CoroutineScope(Dispatchers.IO)
            val instance = Room.databaseBuilder(
                context.applicationContext,
                TheBOCDatabase::class.java,
                DATABASE_NAME
            )
                .createFromAsset("database/THE_BOC_TEST_DB.db")
                .addCallback(TheBOCDatabaseCallback(scope))
                .build()
                .also { INSTANCE = it }
            // INSTANCE = instance
            // return instance
            instance
        }


    }

    @Singleton
    @Provides
    fun provideTheBOCDao(theBOCDatabase: TheBOCDatabase): TheBOCDao {
        return theBOCDatabase.dao()
    }
}