package com.cinevault.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey
    val id: Int,

    val title: String,
    val overview: String,

    val posterPath: String?,
    val backdropPath: String?,

    val releaseDate: String?,
    val rating: Double,

    val mediaType: String,

    val popularity: Double,

    var isBookmarked: Int,

    val movieCategory: MovieCategory
)

enum class MovieCategory {
    TRENDING,
    NOW_PLAYING,
    BOOKMARKED
}

