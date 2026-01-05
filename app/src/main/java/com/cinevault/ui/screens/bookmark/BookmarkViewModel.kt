package com.cinevault.ui.screens.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinevault.data.local.entity.MovieEntity
import com.cinevault.domain.model.Movie
import com.cinevault.domain.repository.MovieRepository
import com.cinevault.ui.screens.home.HomeUiState
import com.cinevault.utils.AppLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {
    val uiState: StateFlow<BookmarkUiState> =
        movieRepository.getBookmarkedMovies()
            .map { movies ->
                AppLogger.putDebugLog("BookmarkViewModel", "$movies")
                BookmarkUiState(result = movies)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = BookmarkUiState(isLoading = true)
            )

    fun removeBookmark(movieId: Int) {
        viewModelScope.launch {
            movieRepository.removeBookmark(movieId)
        }
    }
}