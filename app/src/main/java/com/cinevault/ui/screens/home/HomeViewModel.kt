package com.cinevault.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.cinevault.domain.model.Movie
import com.cinevault.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    val trendingMovies: Flow<PagingData<Movie>> = movieRepository.getTrendingMoviesPaged()
        .cachedIn(viewModelScope)

    val nowPlayingMovies: Flow<PagingData<Movie>> = movieRepository.getNowPlayingMoviesPaged()
        .cachedIn(viewModelScope)

    @FlowPreview
    @OptIn(ExperimentalCoroutinesApi::class)
    val searchResults = searchQuery
        .debounce(400)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isBlank()) {
                flowOf(PagingData.empty())
            } else {
                movieRepository.searchMovies(query)
            }
        }
        .cachedIn(viewModelScope)

    fun onSearchQueryChange(query: String) {
        searchQuery.value = query

        viewModelScope.launch {
            if (query.isNotBlank()) {
                movieRepository.refreshSearch(query)
            }
        }
    }

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