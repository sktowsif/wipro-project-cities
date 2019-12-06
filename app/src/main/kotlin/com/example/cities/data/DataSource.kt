package com.example.cities.data

import org.koin.core.KoinComponent
import org.koin.core.get

interface CountryDataSource {

    suspend fun getData(): Country

}

class CountryRepository(private val apiSource: CountryAPI) : CountryDataSource, KoinComponent {

    override suspend fun getData(): Country {
        val response = apiSource.getData()
        val country = get<CountryMapper>().map(response)
        // Filtering out the data objects which does not have any valid fields
        val cities = country.cities.filter { it.isValid() }
        country.cities.clear()
        country.cities.addAll(cities)
        return country
    }

}