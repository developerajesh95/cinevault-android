package com.cinevault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cinevault.navigation.Routes
import com.cinevault.ui.screens.MovieGridScreen
import com.cinevault.ui.screens.bookmark.BookMarkScreen
import com.cinevault.ui.screens.home.HomeScreen
import com.cinevault.ui.screens.home.HomeViewModel
import com.cinevault.ui.screens.moviedetail.MovieDetailScreen
import com.cinevault.ui.screens.moviedetail.MovieDetailViewModel
import com.cinevault.ui.theme.CineVaultTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CineVaultTheme {
                CineVaultApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CineVaultApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val activity = LocalActivity.current

    val topBarTitle = when {
        currentRoute == Routes.HOME -> "Cine Vault"
        currentRoute == Routes.BOOKMARKS -> "Bookmarks"
        currentRoute == Routes.TRENDING -> "Trending"
        currentRoute == Routes.NOW_PLAYING -> "Now Playing"
        currentRoute?.contains("details") == true -> "Movie Details"
        else -> "Cine Vault"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(topBarTitle) },
                navigationIcon = {
                    // Show back button only if we can actually go back
                    if (navController.previousBackStackEntry != null) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        NavigationSuiteScaffold(
            modifier = Modifier.padding(innerPadding),
            navigationSuiteItems = {
                AppDestinations.entries.forEach { destination ->
                    item(
                        icon = { Icon(destination.icon, contentDescription = destination.label) },
                        label = { Text(destination.label) },
                        selected = currentRoute == destination.route,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(Routes.HOME) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        ) {
            NavHost(
                navController = navController,
                startDestination = Routes.HOME,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(Routes.HOME) {
                    HomeScreen(
                        onMovieClick = { movieId ->
                            navController.navigate(Routes.details(movieId))
                        },
                        onRetry = { activity?.finish() },
                        onSeeMoreTrending = {
                            navController.navigate(Routes.TRENDING)
                        },
                        onSeeMoreNowPlaying = {
                            navController.navigate(Routes.NOW_PLAYING)
                        }
                    )
                }
                composable(Routes.BOOKMARKS) {
                    BookMarkScreen(onMovieClick = { movieId ->
                        navController.navigate(Routes.details(movieId))
                    })
                }
                composable(
                    Routes.DETAILS,
                    arguments = listOf(navArgument("movieId") { type = NavType.IntType })
                ) { backStack ->
                    val movieId = backStack.arguments?.getInt("movieId") ?: 0
                    val viewModel: MovieDetailViewModel = hiltViewModel()
                    MovieDetailScreen(
                        movieId = movieId,
                        onRetry = { viewModel.load(movieId) }
                    )
                }

                composable(Routes.TRENDING) {
                    // Get the existing HomeViewModel to reuse the already loaded data
                    val viewModel: HomeViewModel = hiltViewModel()
                    val state by viewModel.uiState.collectAsState()
                    val movies = state.trendingMovies
                    MovieGridScreen(
                        movies = movies,
                        onMovieClick = { id -> navController.navigate(Routes.details(id)) },
                        onBookmarkClick = { id ->
                            viewModel.bookmarkMovie(movies.find { it.id == id }!!)
                        }
                    )
                }

                composable(Routes.NOW_PLAYING) {
                    val viewModel: HomeViewModel = hiltViewModel()
                    val state by viewModel.uiState.collectAsState()
                    val movies = state.trendingMovies
                    MovieGridScreen(
                        movies = state.nowPlayingMovies,
                        onMovieClick = { id -> navController.navigate(Routes.details(id)) },
                        onBookmarkClick = { id ->
                            viewModel.bookmarkMovie(movies.find { it.id == id }!!)
                        }
                    )
                }
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
    val route: String
) {
    HOME("Home", Icons.Default.Home, Routes.HOME),
    BOOKMARKS("Bookmarks", Icons.Default.Star, Routes.BOOKMARKS)
}

@Composable
@Preview(showBackground = true)
fun GreetingPreview() {
    CineVaultTheme {
        CineVaultApp()
    }
}