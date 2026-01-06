package com.cinevault.ui.screens.bookmark

import com.cinevault.data.local.entity.MovieEntity

data class BookmarkUiState(
    val isLoading: Boolean = false,
    val movies: List<MovieEntity> = emptyList(),
    val isEmpty: Boolean = false,
    val error: String? = null
)
