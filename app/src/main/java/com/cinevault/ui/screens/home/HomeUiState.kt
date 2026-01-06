package com.cinevault.ui.screens.home

import com.cinevault.domain.model.Movie

data class HomeUiState(
    val isLoading: Boolean = true,
    val isBookmarking: Boolean = false,
    val trendingMovies: List<Movie> = emptyList(),
    val nowPlayingMovies: List<Movie> = emptyList(),
    val error: String? = null
)
