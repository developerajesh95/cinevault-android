package com.cinevault.data.remote.dto

import com.cinevault.domain.model.MovieResponse
import com.google.gson.annotations.SerializedName

data class MovieResponseDto(
    @SerializedName("page")
    val page: Int,

    @SerializedName("results")
    val results: List<MovieDto>,

    @SerializedName("total_pages")
    val totalPages: Int
)

fun MovieResponseDto.toMovieResponse(): MovieResponse {
    return MovieResponse(
        page = page,
        results = results.map { it.toMovie() },
        totalPages = totalPages
    )
}
