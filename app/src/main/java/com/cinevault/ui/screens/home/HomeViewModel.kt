package com.cinevault.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinevault.domain.model.Movie
import com.cinevault.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> =
        combine(
            movieRepository.getTrendingMovies(),
            movieRepository.getNowPlayingMovies()
        ) { trending, nowPlaying ->
            HomeUiState(
                trendingMovies = trending,
                nowPlayingMovies = nowPlaying,
                isLoading = false
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            HomeUiState(isLoading = true)
        )

    init {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            try {
                movieRepository.refreshTrendingMovies("day")
                movieRepository.refreshNowPlayingMovies()
            } catch (_: Exception) {
                // DB still serves cached data
            }
        }
    }

    fun bookmarkMovie(movie: Movie) {
        viewModelScope.launch {
            movieRepository.bookmarkMovie(movie)
        }
    }

}