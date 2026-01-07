package com.cinevault.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cinevault.domain.model.MovieDetails

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey
    val id: Int,

    val title: String,
    val overview: String,

    val posterPath: String,
    val backdropPath: String,

    val releaseDate: String,
    val rating: Double,

    val mediaType: String,

    val isTrending: Boolean = false,
    val isNowPlaying: Boolean = false,

    val trendingPage: Int = 0,
    val nowPlayingPage: Int = 0,

    var isBookmarked: Boolean = false,

    val lastUpdated: Long
)

fun MovieEntity.toMovieDetails(): MovieDetails {
    return MovieDetails(
        id = id.toLong(),
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        voteAverage = rating,
        isBookmarked = isBookmarked,
        originalTitle = title
    )
}
