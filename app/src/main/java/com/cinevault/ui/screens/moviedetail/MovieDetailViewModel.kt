package com.cinevault.ui.screens.moviedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinevault.domain.model.MovieDetails
import com.cinevault.domain.model.toMovie
import com.cinevault.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val movieId = MutableStateFlow<Int?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<MovieDetailsUiState> =
        movieId
            .filterNotNull()
            .flatMapLatest { id ->
                combine(
                    movieRepository.getMovieDetails(id),
                    movieRepository.isBookmarkedFlow(id)
                ) { movie, isBookmarked ->
                    MovieDetailsUiState(
                        isLoading = false,
                        response = movie?.copy(isBookmarked = isBookmarked)
                    )
                }
            }
            .catch { e ->
                emit(
                    MovieDetailsUiState(
                        isLoading = false,
                        error = e.message ?: "Failed to load movie"
                    )
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = MovieDetailsUiState(isLoading = true)
            )

    fun load(movieId: Int) {
        this.movieId.value = movieId
    }

    fun toggleBookmark(movie: MovieDetails) {
        viewModelScope.launch {
            if (!movie.isBookmarked) {
                movieRepository.bookmarkMovie(movie.toMovie())
            }
        }
    }
}