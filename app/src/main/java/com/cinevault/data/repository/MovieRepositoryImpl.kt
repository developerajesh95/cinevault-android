package com.cinevault.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.cinevault.data.local.dao.MovieDao
import com.cinevault.data.local.entity.MovieEntity
import com.cinevault.data.local.entity.toMovieDetails
import com.cinevault.data.mapper.toDomain
import com.cinevault.data.mediator.NowPlayingMoviesRemoteMediator
import com.cinevault.data.mediator.TrendingMoviesRemoteMediator
import com.cinevault.data.remote.TmdbApi
import com.cinevault.data.remote.dto.toMovie
import com.cinevault.data.remote.dto.toMovieDetails
import com.cinevault.domain.model.Movie
import com.cinevault.domain.model.MovieDetails
import com.cinevault.domain.model.toEntity
import com.cinevault.domain.repository.MovieRepository
import com.cinevault.utils.AppLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.map

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val api: TmdbApi,
    private val dao: MovieDao
) : MovieRepository {

    override fun getTrendingMovies() =
        dao.getTrendingMovies().map { it.map(MovieEntity::toDomain) }

    override suspend fun refreshTrendingMovies(timeWindow: String) {
        val response = api.getTrendingMovies(timeWindow).results

        val entities = response.map { dto ->
            val bookmarked = dao.getBookmarkState(dto.id) ?: false

            dto.toMovie().toEntity().copy(
                isTrending = true,
                isBookmarked = bookmarked,
                lastUpdated = System.currentTimeMillis()
            )
        }

        dao.clearTrendingFlag()
        dao.insertMovies(entities)
    }

    override fun getNowPlayingMovies() =
        dao.getNowPlayingMovies().map { it.map(MovieEntity::toDomain) }

    override suspend fun refreshNowPlayingMovies() {
        val response = api.getNowPlayingMovies().results

        val entities = response.map { dto ->
            val bookmarked = dao.getBookmarkState(dto.id) ?: false

            dto.toMovie().toEntity().copy(
                isNowPlaying = true,
                isBookmarked = bookmarked,
                lastUpdated = System.currentTimeMillis()
            )
        }

        dao.clearNowPlayingFlag()
        dao.insertMovies(entities)
    }

    override fun getMovieDetails(movieId: Int): Flow<MovieDetails?> {
        return dao.observeMovie(movieId)
            .map { entity -> entity?.toMovieDetails() }
            .onStart {
                // refresh in background
                refreshMovieDetails(movieId)
            }
    }

    override fun isBookmarkedFlow(movieId: Int) = dao.isBookmarkedFlow(movieId)


    override suspend fun refreshMovieDetails(movieId: Int) {
        try {
            // Get existing movie to preserve bookmark status
            val existingMovie = dao.getMovieById(movieId)

            // Fetch fresh data from API
            val details = api.getMovieDetails(movieId)

            // Convert to entity while preserving bookmark status
            val entity = details.toMovieDetails().toEntity().copy(
                isBookmarked = existingMovie?.isBookmarked ?: false,
                isTrending = existingMovie?.isTrending ?: false,
                isNowPlaying = existingMovie?.isNowPlaying ?: false
            )

            dao.insertMovie(entity)
        } catch (e: Exception) {
            AppLogger.putErrorLog("MovieRepositoryImpl", "refreshMovieDetails: ${e.message}")
        }
    }

    override fun getBookmarkedMovies() = dao.getBookmarkedMovies()

    override suspend fun bookmarkMovie(movie: Movie) = dao.bookmark(movie.id)

    override suspend fun removeBookmark(movieId: Int) = dao.removeBookmark(movieId)

    override fun isBookmarked(movieId: Int) = dao.isBookmarked(movieId).map { it ?: false }

    override fun searchMovies(query: String): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                dao.searchMoviesPaging(query)
            }
        ).flow.map { pagingData ->
            pagingData.map { entity -> entity.toDomain() }
        }
    }

    override suspend fun refreshSearch(query: String) {
        val remote = api.searchMovies(query)

        val entities = remote.results.map {
            it.toMovie().toEntity().copy(
                isTrending = false,
                isNowPlaying = false
            )
        }

        dao.insertMovies(entities)
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getTrendingMoviesPaged(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                prefetchDistance = 5,
                initialLoadSize = 20
            ),
            remoteMediator = TrendingMoviesRemoteMediator(
                movieApi = api,
                movieDao = dao
            ),
            pagingSourceFactory = { dao.getTrendingMoviesPaged() }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getNowPlayingMoviesPaged(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                prefetchDistance = 5,
                initialLoadSize = 20
            ),
            remoteMediator = NowPlayingMoviesRemoteMediator(
                movieApi = api,
                movieDao = dao
            ),
            pagingSourceFactory = { dao.getNowPlayingMoviesPaged() }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }


}
