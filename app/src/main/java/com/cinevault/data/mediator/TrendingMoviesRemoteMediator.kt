package com.cinevault.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.cinevault.data.local.dao.MovieDao
import com.cinevault.data.local.entity.MovieEntity
import com.cinevault.data.remote.TmdbApi
import com.cinevault.data.remote.dto.toMovie
import com.cinevault.domain.model.toEntity

@OptIn(ExperimentalPagingApi::class)
class TrendingMoviesRemoteMediator(
    private val movieApi: TmdbApi,
    private val movieDao: MovieDao
) : RemoteMediator<Int, MovieEntity>() {

    private var currentPage = 1

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    currentPage = 1
                    1
                }
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    currentPage + 1
                }
            }

            val response = movieApi.getTrendingMovies(timeWindow = "week", page = page)
            val movies = response.results.map { movieDto ->
                // Check if movie exists and is bookmarked
                val existingMovie = movieDao.getMovieById(movieDto.id)
                movieDto.toMovie().toEntity().copy(
                    isTrending = true,
                    trendingPage = page,
                    isBookmarked = existingMovie?.isBookmarked ?: false
                )
            }

            val endOfPaginationReached = movies.isEmpty() || page >= response.totalPages

            if (loadType == LoadType.REFRESH) {
                // Don't clear bookmarked movies
                movieDao.clearTrendingFlagExceptBookmarked()
            }

            movieDao.insertMovies(movies)
            currentPage = page

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}