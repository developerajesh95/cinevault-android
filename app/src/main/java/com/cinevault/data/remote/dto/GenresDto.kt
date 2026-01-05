package com.cinevault.data.remote.dto

import com.cinevault.domain.model.Genres
import com.google.gson.annotations.SerializedName

data class GenresDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String
)

fun GenresDto.toGenres() = Genres(
    id = id,
    name = name
)