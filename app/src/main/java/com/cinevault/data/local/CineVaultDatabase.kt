package com.cinevault.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cinevault.data.local.dao.MovieDao
import com.cinevault.data.local.entity.MovieEntity

@Database(
    entities = [MovieEntity::class],
    version = 5,
    exportSchema = false
)
abstract class CineVaultDatabase: RoomDatabase() {
    abstract fun movieDao(): MovieDao
}