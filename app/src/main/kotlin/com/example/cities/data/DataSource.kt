package com.example.cities.data

interface CountryDataSource {

    suspend fun getData(): EntityCountry

}

class CountryRepository(private val apiSource: CountryAPI) : CountryDataSource {

    override suspend fun getData(): EntityCountry = apiSource.getData()


}