package com.example.cities.data

import retrofit2.http.GET

interface CountryAPI {

    @GET("s/2iodh4vg0eortkl/facts.json")
    suspend fun getData(): EntityCountry

}