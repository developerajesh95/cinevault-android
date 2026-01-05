package com.cinevault.data.remote.dto

import com.cinevault.domain.model.ProductionCompany
import com.google.gson.annotations.SerializedName

data class ProductionCompanyDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("logo_path")
    val logoPath: String?,

    @SerializedName("name")
    val name: String,

    @SerializedName("origin_country")
    val originCountry: String
)

fun ProductionCompanyDto.toProductionCompany() = ProductionCompany(
    id = id,
    logoPath = logoPath,
    name = name,
    originCountry = originCountry
)