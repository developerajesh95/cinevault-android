package com.cinevault.data.remote.dto

import com.cinevault.domain.model.SpokenLanguage
import com.google.gson.annotations.SerializedName

data class SpokenLanguageDto(
    @SerializedName("english_name")
    val englishName: String,

    @SerializedName("iso_639_1")
    val iso6391: String,

    @SerializedName("name")
    val name: String
)

fun SpokenLanguageDto.toSpokenLanguage() = SpokenLanguage(
    englishName = englishName,
    iso6391 = iso6391,
    name = name
)