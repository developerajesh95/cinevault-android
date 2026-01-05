package com.cinevault.data.remote.dto

import com.cinevault.domain.model.BelongsToCollection
import com.google.gson.annotations.SerializedName

data class BelongsToCollectionDto(
    @SerializedName("id")
    val id: Long,

    @SerializedName("name")
    val name: String,

    @SerializedName("poster_path")
    val posterPath: String,

    @SerializedName("backdrop_path")
    val backdropPath: String
)

fun BelongsToCollectionDto.toBelongsToCollection() = BelongsToCollection(
    id = id,
    name = name,
    posterPath = posterPath,
    backdropPath = backdropPath
)
