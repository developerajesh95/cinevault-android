package com.cinevault.domain.model

import com.cinevault.data.local.entity.MovieCategory
import com.cinevault.data.local.entity.MovieEntity

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String?,
    val rating: Double,
    val mediaType: String?,
    val genreIds: List<Int>,
    val adult: Boolean,
    val popularity: Double,
    val video: Boolean,
    val voteCount: Int,
    val originalLanguage: String,
    val originalTitle: String,
    val originCountry: List<String>? = null,
    val originalName: String? = null,
    val name: String? = null,
    val firstAirDate: String? = null,
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
        popularity = popularity,
        isBookmarked = 0,
        mediaType = mediaType ?: "",
        movieCategory = MovieCategory.TRENDING
    )
}
