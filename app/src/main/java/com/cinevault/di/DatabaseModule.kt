package com.cinevault.di

import android.content.Context
import androidx.room.Room
import com.cinevault.data.local.CineVaultDatabase
import com.cinevault.data.local.dao.MovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): CineVaultDatabase {
        return Room.databaseBuilder(
            context,
            CineVaultDatabase::class.java,
            "cinevault_db"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideMovieDao(database: CineVaultDatabase): MovieDao {
        return database.movieDao()
    }
}
