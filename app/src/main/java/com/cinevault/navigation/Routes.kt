package com.cinevault.navigation

object Routes {
    const val HOME = "home"
    const val BOOKMARKS = "bookmarks"
    const val DETAILS = "details/{movieId}"
    const val TRENDING = "trending"
    const val NOW_PLAYING = "nowPlaying"


    fun details(movieId: Int) = "details/$movieId"
}