package com.cinevault.ui.screens.bookmark

import com.cinevault.data.local.entity.MovieEntity

data class BookmarkUiState(
    val isLoading: Boolean = false,
    val result: List<MovieEntity>? = null,
    val error: String? = null
)
