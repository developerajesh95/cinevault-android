package com.cinevault.domain.model

import com.cinevault.data.local.entity.MovieEntity

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String,
    val backdropPath: String,
    val releaseDate: String,
    val rating: Double,
    val mediaType: String?,
    val name: String? = null,
    val popularity: Double,
    var isBookmarked: Boolean = false
)

fun Movie.toEntity(): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        rating = rating,
        isBookmarked = isBookmarked,
        mediaType = mediaType ?: "",
        isTrending = false,
        isNowPlaying = false,
        lastUpdated = System.currentTimeMillis()
    )
}
