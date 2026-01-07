package com.cinevault.ui.screens.home

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.cinevault.domain.model.Movie
import com.cinevault.ui.screens.MovieGridScreen
import com.cinevault.ui.screens.MovieItem
import com.cinevault.ui.screens.SeeMoreItem
import com.cinevault.ui.screens.ShowErrorMessage
import com.cinevault.ui.screens.ShowProgressIndicator
import com.cinevault.utils.AppLogger
import kotlinx.coroutines.FlowPreview

@OptIn(FlowPreview::class)
@Composable
fun HomeScreen(
    onMovieClick: (Int) -> Unit,
    onRetry: () -> Unit,
    onSeeMoreTrending: () -> Unit,
    onSeeMoreNowPlaying: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val searchResults = viewModel.searchResults.collectAsLazyPagingItems()
    var query by remember { mutableStateOf("") }


    Box(modifier = Modifier.fillMaxSize()) {
        when {
            state.isLoading -> {
                ShowProgressIndicator()
            }

            state.error != null -> {
                ShowErrorMessage(state.error, onRetry = onRetry)
            }

            else -> {
                BackHandler(enabled = query.isNotBlank()) {
                    query = ""
                    viewModel.onSearchQueryChange("")
                }

                Column(modifier = Modifier.fillMaxSize()) {
                    HomeSearchBar(
                        query = query,
                        onQueryChange = {
                            query = it
                            viewModel.onSearchQueryChange(it)
                        }
                    )

                    if (query.isNotBlank()) {
                        SearchContent (
                            pagingItems = searchResults,
                            onMovieClick = onMovieClick,
                            onBookmarkClick = { movie ->
                                viewModel.bookmarkMovie(movie)
                            }
                        )
                    } else {
                        HomeContent(
                            trending = state.trendingMovies,
                            nowPlaying = state.nowPlayingMovies,
                            onMovieClick = onMovieClick,
                            onBookmarkClick = { movie ->
                                viewModel.bookmarkMovie(movie)
                            },
                            onSeeMoreTrending = { onSeeMoreTrending() },
                            onSeeMoreNowPlaying = { onSeeMoreNowPlaying() }
                        )
                    }
                }
            }
        }

        if (state.isBookmarking) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable(enabled = false) { },
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Bookmarking...", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun HomeContent(
    trending: List<Movie>,
    nowPlaying: List<Movie>,
    onMovieClick: (Int) -> Unit,
    onBookmarkClick: (Movie) -> Unit,
    onSeeMoreTrending: () -> Unit,
    onSeeMoreNowPlaying: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text("Trending", style = MaterialTheme.typography.titleLarge)
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = CenterVertically
        ) {

            items(trending.take(4)) { currentMovie ->
                MovieItem(
                    currentMovie,
                    onClick = { onMovieClick(currentMovie.id) },
                    onBookmarkClick = { movie ->
                        onBookmarkClick(movie)
                    }
                )
            }

            item {
                SeeMoreItem(onClick = onSeeMoreTrending)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Now Playing", style = MaterialTheme.typography.titleLarge)

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = CenterVertically
        ) {
            items(nowPlaying.take(4)) { currentMovie ->
                MovieItem(
                    currentMovie,
                    onClick = { onMovieClick(currentMovie.id) },
                    onBookmarkClick = { movie ->
                        onBookmarkClick(movie)
                    }
                )
            }

            item {
                SeeMoreItem(onClick = onSeeMoreNowPlaying)
            }
        }
    }
}

@Composable
fun HomeSearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        placeholder = { Text("Search movies...") },
        singleLine = true,
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Search")
        }
    )
}


@Composable
fun SearchContent(
    pagingItems: LazyPagingItems<Movie>,
    onMovieClick: (Int) -> Unit,
    onBookmarkClick: (Movie) -> Unit
) {
    when {
        pagingItems.loadState.refresh is LoadState.Loading -> {
            ShowProgressIndicator()
        }

        pagingItems.itemCount == 0 -> {
            AppLogger.showToast(LocalContext.current, "No results found")
        }

        else -> {
            MovieGridScreen(
                movies = pagingItems,
                onMovieClick = onMovieClick,
                onBookmarkClick = onBookmarkClick
            )
        }
    }
}