package com.cinevault.ui.screens.home

import com.cinevault.domain.model.MovieResponse

data class HomeUiState(
    val isLoading: Boolean = false,
    val isBookmarking: Boolean = false,
    val trendingMovies: MovieResponse? = null,
    val nowPlayingMovies: MovieResponse? = null,
    val error: String? = null
)
