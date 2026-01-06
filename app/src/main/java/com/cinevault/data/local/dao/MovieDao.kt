package com.cinevault.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cinevault.data.local.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    // READ (UI uses these)
    @Query("SELECT * FROM movies WHERE isTrending = 1")
    fun getTrendingMovies(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE isNowPlaying = 1")
    fun getNowPlayingMovies(): Flow<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    @Query("SELECT * FROM movies WHERE isBookmarked = 1")
    fun getBookmarkedMovies(): Flow<List<MovieEntity>>

    @Query("SELECT isBookmarked FROM movies WHERE id = :movieId")
    fun isBookmarked(movieId: Int): Flow<Boolean?>

    // WRITE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Query("UPDATE movies SET isTrending = 0")
    suspend fun clearTrendingFlag()

    @Query("UPDATE movies SET isNowPlaying = 0")
    suspend fun clearNowPlayingFlag()

    @Query("UPDATE movies SET isBookmarked = 1 WHERE id = :movieId")
    suspend fun bookmark(movieId: Int)

    @Query("UPDATE movies SET isBookmarked = 0 WHERE id = :movieId")
    suspend fun removeBookmark(movieId: Int)

    @Query("SELECT isBookmarked FROM movies WHERE id = :movieId")
    suspend fun getBookmarkState(movieId: Int): Boolean?

    @Query("SELECT * FROM movies WHERE id = :movieId LIMIT 1")
    fun observeMovie(movieId: Int): Flow<MovieEntity?>

    @Query("SELECT EXISTS(SELECT 1 FROM movies WHERE id = :movieId AND isBookmarked = 1)")
    fun isBookmarkedFlow(movieId: Int): Flow<Boolean>

}
