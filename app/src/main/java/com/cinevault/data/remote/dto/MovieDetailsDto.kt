package com.cinevault.data.remote.dto

import com.cinevault.domain.model.MovieDetails
import com.google.gson.annotations.SerializedName

data class MovieDetailsDto(
    @SerializedName("adult")
    val adult: Boolean,

    @SerializedName("backdrop_path")
    val backdropPath: String,

    @SerializedName("belongs_to_collection")
    val belongsToCollection: BelongsToCollectionDto?,

    @SerializedName("budget")
    val budget: Long,

    @SerializedName("genres")
    val genres: List<GenresDto>,

    @SerializedName("homepage")
    val homepage: String,

    @SerializedName("id")
    val id: Long,

    @SerializedName("imdb_id")
    val imdbId: String,

    @SerializedName("original_language")
    val originalLanguage: String,

    @SerializedName("original_title")
    val originalTitle: String,

    @SerializedName("overview")
    val overview: String,

    @SerializedName("popularity")
    val popularity: Double,

    @SerializedName("poster_path")
    val posterPath: String,

    @SerializedName("production_companies")
    val productionCompanies: List<ProductionCompanyDto>,

    @SerializedName("production_countries")
    val productionCountries: List<ProductionCountryDto>,

    @SerializedName("release_date")
    val releaseDate: String,

    @SerializedName("revenue")
    val revenue: Long,

    @SerializedName("runtime")
    val runtime: Long,

    @SerializedName("spoken_languages")
    val spokenLanguages: List<SpokenLanguageDto>,

    @SerializedName("origin_country")
    val originCountry: List<String>,

    @SerializedName("status")
    val status: String,

    @SerializedName("tagline")
    val tagline: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("video")
    val video: Boolean,

    @SerializedName("vote_average")
    val voteAverage: Double,

    @SerializedName("vote_count")
    val voteCount: Long
)

fun MovieDetailsDto.toMovieDetails() = MovieDetails(
    id = id,
    adult = adult,
    title = title,
    video = video,
    budget = budget,
    genres = genres.map { it.toGenres() },
    imdbId = imdbId,
    status = status,
    revenue = revenue,
    runtime = runtime,
    tagline = tagline,
    homepage = homepage,
    overview = overview,
    voteCount = voteCount,
    popularity = popularity,
    posterPath = posterPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    backdropPath = backdropPath,
    originalTitle = originalTitle,
    originCountry = originCountry,
    originalLanguage = originalLanguage,
    belongsToCollection = belongsToCollection?.toBelongsToCollection(),
    spokenLanguages = spokenLanguages.map { it.toSpokenLanguage() },
    productionCompanies = productionCompanies.map { it.toProductionCompany() },
    productionCountries = productionCountries.map { it.toProductionCountry() }
)