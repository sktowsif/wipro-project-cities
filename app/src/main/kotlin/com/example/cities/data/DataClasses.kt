package com.example.cities.data

import com.google.gson.annotations.SerializedName

data class EntityCountry(
    @SerializedName("title") val title: String,
    @SerializedName("rows") val cities: List<Any>
)

data class EntityCity(
    @SerializedName("title") val title: String?,
    @SerializedName("description") val desc: String?,
    @SerializedName("imageHref") val imageUrl: String?
)