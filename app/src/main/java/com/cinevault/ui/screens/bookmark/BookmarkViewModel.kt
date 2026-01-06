package com.cinevault.ui.screens.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinevault.domain.repository.MovieRepository
import com.cinevault.utils.AppLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
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
                AppLogger.putDebugLog("BookmarkViewModel", "Bookmarks: $movies")

                BookmarkUiState(
                    isLoading = false,
                    movies = movies,
                    isEmpty = movies.isEmpty()
                )
            }
            .catch { e ->
                emit(
                    BookmarkUiState(
                        isLoading = false,
                        error = e.message ?: "Failed to load bookmarks"
                    )
                )
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
