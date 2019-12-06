package com.example.cities.data

import com.google.gson.annotations.SerializedName

data class EntityCountry(
    @SerializedName("title") val title: String?,
    @SerializedName("rows") val cities: List<EntityCity>?
)

data class EntityCity(
    @SerializedName("title") val title: String?,
    @SerializedName("description") val desc: String?,
    @SerializedName("imageHref") val imageUrl: String?
)

data class Country(val title: String, val cities: ArrayList<City>)

data class City(val title: String, val desc: String, val imageUrl: String?) {

    fun isValid() = title.isNotEmpty() || desc.isNotEmpty() || !imageUrl.isNullOrEmpty()

}