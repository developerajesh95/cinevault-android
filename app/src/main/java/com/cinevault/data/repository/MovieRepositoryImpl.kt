package com.cinevault.data.repository

import com.cinevault.data.local.dao.MovieDao
import com.cinevault.data.local.entity.MovieEntity
import com.cinevault.data.remote.TmdbApi
import com.cinevault.data.remote.dto.toMovieDetails
import com.cinevault.data.remote.dto.toMovieResponse
import com.cinevault.domain.model.Movie
import com.cinevault.domain.model.MovieDetails
import com.cinevault.domain.model.MovieResponse
import com.cinevault.domain.model.toEntity
import com.cinevault.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val api: TmdbApi,
    private val movieDao: MovieDao
) : MovieRepository {

    override suspend fun getTrendingMovies(timeWindow: String): MovieResponse {
        return api.getTrendingMovies(timeWindow).toMovieResponse()
    }

    override suspend fun getNowPlayingMovies(): MovieResponse {
        return api.getNowPlayingMovies().toMovieResponse()
    }

    override suspend fun getMovieDetails(movieId: Int): MovieDetails {
        return api.getMovieDetails(movieId).toMovieDetails()
    }

    override suspend fun refreshTrendingMovies() {

    }

    override suspend fun refreshNowPlayingMovies() {

    }

    override suspend fun bookmarkMovie(movie: Movie) {
        movieDao.insertMovie(movie.toEntity().also { it.isBookmarked = 1 })
    }

    override suspend fun removeBookmark(movieId: Int) {
        movieDao.deleteMovie(movieId)
    }

    override fun getBookmarkedMovies(): Flow<List<MovieEntity>> {
        return movieDao.getBookmarkedMovies()
    }

    override suspend fun isBookmarked(movieId: Int): Boolean = movieDao.isBookmarked(movieId)

}