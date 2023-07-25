package net.thebookofcode.www.list_panelattempt.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.thebookofcode.www.list_panelattempt.room.dao.TheBOCDao
import net.thebookofcode.www.list_panelattempt.room.repository.TheBOCRepository
import net.thebookofcode.www.list_panelattempt.room.repository.TheBOCRepositoryImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {
    @Singleton
    @Provides
    fun provideTheBOCRepository(dao: TheBOCDao): TheBOCRepository {
        return TheBOCRepositoryImpl(dao)
    }
}