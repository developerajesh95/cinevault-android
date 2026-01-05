package com.cinevault.data.remote.dto

import com.cinevault.domain.model.ProductionCountry
import com.google.gson.annotations.SerializedName

data class ProductionCountryDto(
    @SerializedName("iso_3166_1")
    val iso31661: String,

    @SerializedName("name")
    val name: String,
)

fun ProductionCountryDto.toProductionCountry() = ProductionCountry(
    iso31661 = iso31661,
    name = name,
)