package com.cinevault.data.remote.dto

import com.cinevault.domain.model.Movie
import com.google.gson.annotations.SerializedName

data class MovieDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("overview")
    val overview: String,

    @SerializedName("poster_path")
    val posterPath: String?,

    @SerializedName("backdrop_path")
    val backdropPath: String?,

    @SerializedName("release_date")
    val releaseDate: String,

    @SerializedName("vote_average")
    val rating: Double,

    @SerializedName("media_type")
    val mediaType: String?,

    @SerializedName("genre_ids")
    val genreIds: List<Int>,

    @SerializedName("adult")
    val adult: Boolean,

    @SerializedName("popularity")
    val popularity: Double,

    @SerializedName("video")
    val video: Boolean,

    @SerializedName("vote_count")
    val voteCount: Int,

    @SerializedName("original_language")
    val originalLanguage: String,

    @SerializedName("original_title")
    val originalTitle: String,

    @SerializedName("origin_country")
    val originCountry: List<String>? = null,

    @SerializedName("original_name")
    val originalName: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("first_air_date")
    val firstAirDate: String? = null
)

fun MovieDto.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath.toString(),
        backdropPath = backdropPath.toString(),
        releaseDate = releaseDate,
        rating = rating,
        mediaType = mediaType,
        popularity = popularity,
        name = name,
    )
}