package com.cinevault.domain.model

data class MovieResponse(
    val page: Int,
    val results: List<Movie>,
    val totalPages: Int
)
