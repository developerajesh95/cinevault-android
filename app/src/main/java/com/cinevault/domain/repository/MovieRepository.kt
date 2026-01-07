package com.cinevault.domain.repository

import androidx.paging.PagingData
import com.cinevault.data.local.entity.MovieEntity
import com.cinevault.domain.model.Movie
import com.cinevault.domain.model.MovieDetails
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    // UI always collects from DB
    fun getTrendingMovies(): Flow<List<Movie>>

    fun getNowPlayingMovies(): Flow<List<Movie>>

    // pagination of movies
    fun getTrendingMoviesPaged(): Flow<PagingData<Movie>>

    fun getNowPlayingMoviesPaged(): Flow<PagingData<Movie>>

    fun getBookmarkedMovies(): Flow<List<MovieEntity>>

    fun isBookmarked(movieId: Int): Flow<Boolean>

    fun getMovieDetails(movieId: Int): Flow<MovieDetails?>

    fun isBookmarkedFlow(movieId: Int): Flow<Boolean>

    suspend fun refreshMovieDetails(movieId: Int)

    // Network → DB sync
    suspend fun refreshTrendingMovies(timeWindow: String)

    suspend fun refreshNowPlayingMovies()

    // User actions
    suspend fun bookmarkMovie(movie: Movie)

    suspend fun removeBookmark(movieId: Int)

    // search functionality
    fun searchMovies(query: String): Flow<PagingData<Movie>>

    suspend fun refreshSearch(query: String)
}
