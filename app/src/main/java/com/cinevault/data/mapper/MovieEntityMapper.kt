package com.cinevault.data.mapper

import com.cinevault.data.local.entity.MovieEntity
import com.cinevault.domain.model.Movie

fun MovieEntity.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        popularity = rating,
        mediaType = mediaType,
        rating = rating,
        name = "",
        isBookmarked = isBookmarked
    )
}