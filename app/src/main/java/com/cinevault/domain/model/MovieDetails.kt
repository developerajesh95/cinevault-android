package com.cinevault.domain.model

import com.cinevault.data.local.entity.MovieEntity

data class MovieDetails(
    val backdropPath: String,
    val id: Long,
    val originalTitle: String,
    val overview: String,
    val posterPath: String,
    val releaseDate: String,
    val voteAverage: Double,
    val title: String,
    var isBookmarked: Boolean = false
)

fun MovieDetails.toEntity(): MovieEntity {
    return MovieEntity(
        id = id.toInt(),
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        rating = voteAverage,
        mediaType = "movie",
        isBookmarked = isBookmarked,
        lastUpdated = System.currentTimeMillis()
    )
}

fun MovieDetails.toMovie() = Movie(
    id = id.toInt(),
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate,
    rating = voteAverage,
    mediaType = "movie",
    popularity = voteAverage,
    name = title,
    isBookmarked = isBookmarked
)
