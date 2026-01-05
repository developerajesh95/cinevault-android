package com.cinevault.data.remote

import com.cinevault.data.remote.dto.MovieDetailsDto
import com.cinevault.data.remote.dto.MovieResponseDto
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {

    // for trending movies
    @GET(value = "trending/movie/{time_window}")
    suspend fun getTrendingMovies(
        @Path("time_window") timeWindow: String
    ): MovieResponseDto

    // for now playing movies
    @GET(value = "movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query(value = "page") page: Int = 1
    ): MovieResponseDto

    // for getting movie details with id
    @GET(value = "movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path(value = "movie_id") movieId: Int
    ): MovieDetailsDto

    // for searching movies
    @GET(value = "search/movie")
    suspend fun searchMovies(
        @Query(value = "query") query: String
    ): MovieResponseDto
}
