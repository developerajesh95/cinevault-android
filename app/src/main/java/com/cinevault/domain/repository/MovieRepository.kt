package com.cinevault.domain.repository

import com.cinevault.data.local.entity.MovieEntity
import com.cinevault.data.remote.dto.MovieResponseDto
import com.cinevault.domain.model.Movie
import com.cinevault.domain.model.MovieDetails
import com.cinevault.domain.model.MovieResponse
import com.cinevault.domain.model.toEntity
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getTrendingMovies(timeWindow: String): MovieResponse

    suspend fun getNowPlayingMovies(): MovieResponse

    suspend fun getMovieDetails(movieId: Int): MovieDetails

    suspend fun refreshTrendingMovies()

    suspend fun refreshNowPlayingMovies()

    suspend fun bookmarkMovie(movie: Movie)

    suspend fun removeBookmark(movieId: Int)

    fun getBookmarkedMovies(): Flow<List<MovieEntity>>

    suspend fun isBookmarked(movieId: Int): Boolean
}