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
        rating = rating,
        mediaType = mediaType,
        genreIds = emptyList(),
        adult = false,
        popularity = popularity,
        video = false,
        voteCount = 0,
        originalLanguage = "",
        originalTitle = "",
        originCountry = emptyList(),
        originalName = "",
        name = "",
        firstAirDate = ""
    )
}