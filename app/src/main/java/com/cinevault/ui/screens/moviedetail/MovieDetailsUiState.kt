package com.cinevault.ui.screens.moviedetail

import com.cinevault.domain.model.MovieDetails

data class MovieDetailsUiState(
    val isLoading: Boolean = false,
    val response: MovieDetails? = null,
    val error: String? = null
)
