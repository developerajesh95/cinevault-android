package com.cinevault.ui.screens.moviedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinevault.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(MovieDetailsUiState(isLoading = true))
    val uiState: StateFlow<MovieDetailsUiState> = _uiState.asStateFlow()


    fun getMovie(movieId: Int) {
        viewModelScope.launch {
            _uiState.value = MovieDetailsUiState(isLoading = true)
            try {
                val movieDetails = movieRepository.getMovieDetails(movieId).also {
                    it.isBookmarked = isBookmarked(movieId)
                }
                _uiState.value = MovieDetailsUiState(
                    response = movieDetails,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = MovieDetailsUiState(
                    error = e.message ?: "Something went wrong"
                )
            }
        }
    }

    suspend fun isBookmarked(movieId: Int): Boolean {
        return movieRepository.isBookmarked(movieId)
    }
}