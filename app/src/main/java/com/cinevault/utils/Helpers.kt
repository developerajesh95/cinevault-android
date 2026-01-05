package com.cinevault.utils

import com.cinevault.domain.model.Movie
import com.cinevault.domain.model.MovieResponse
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object Helpers {


    fun fakeMovieResponse() = MovieResponse(
        page = 1,
        totalPages = 1,
        results = listOf(
            Movie(
                id = 1,
                title = "Interstellar",
                posterPath = "/poster.jpg",
                overview = "",
                rating = 8.6,
                releaseDate = "2014-11-07",
                backdropPath = null,
                mediaType = "movie",
                genreIds = emptyList(),
                adult = false,
                popularity = 0.0,
                video = false,
                voteCount = 0,
                originalLanguage = "en",
                originalTitle = "Interstellar",
                originCountry = emptyList(),
                originalName = "",
                name = "",
                firstAirDate = ""
            )
        )
    )

    fun formatDate(date: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val outputFormatter = DateTimeFormatter.ofPattern("d MMM, yyyy", Locale.ENGLISH)

        val parsedDate = LocalDate.parse(date, inputFormatter)
        return parsedDate.format(outputFormatter)
    }

}