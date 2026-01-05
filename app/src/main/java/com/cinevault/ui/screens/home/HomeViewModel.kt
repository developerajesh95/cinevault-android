package com.cinevault.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinevault.domain.model.Movie
import com.cinevault.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }


    private fun loadHomeData() {
        viewModelScope.launch {
            _uiState.value = HomeUiState(isLoading = true)
            try {
                val trending = movieRepository.getTrendingMovies("day").also { response ->
                    response.results.map { it.isBookmarked = isBookmarked(it.id) }
                }
                val nowPlaying = movieRepository.getNowPlayingMovies().also { response ->
                    response.results.map { it.isBookmarked = isBookmarked(it.id) }
                }
                _uiState.value = HomeUiState(
                    trendingMovies = trending,
                    nowPlayingMovies = nowPlaying,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = HomeUiState(
                    error = e.message ?: "Something went wrong"
                )
            }
        }
    }

    fun bookmarkMovie(movie: Movie) {
        viewModelScope.launch {
            _uiState.update { it.copy(isBookmarking = true) }
            try {
                if (!isBookmarked(movie.id)) {
                    movieRepository.bookmarkMovie(movie)
                } else {
                    _uiState.update { it.copy(isBookmarking = false) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Failed to bookmark: ${e.message}") }
            } finally {
                _uiState.update { it.copy(isBookmarking = false) }
            }
        }
    }

    suspend fun isBookmarked(movieId: Int): Boolean {
        return movieRepository.isBookmarked(movieId)
    }


}