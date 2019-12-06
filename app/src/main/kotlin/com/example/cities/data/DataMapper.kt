package com.example.cities.data

interface Mapper<I, O> {
    fun map(input: I): O
}

interface NullableInputListMapper<I, O> : Mapper<List<I>?, List<O>>

open class NullableInputListMapperImpl<I, O>(private val mapper: Mapper<I, O>) :
    NullableInputListMapper<I, O> {
    override fun map(input: List<I>?): List<O> {
        return input?.map { mapper.map(it) }.orEmpty()
    }
}

class CountryMapper(private val cityListMapper: CityListMapper) : Mapper<EntityCountry, Country> {
    override fun map(input: EntityCountry): Country {
        return Country(
            title = input.title ?: "",
            cities = ArrayList(cityListMapper.map(input.cities))
        )
    }
}

class CityMapper : Mapper<EntityCity, City> {

    override fun map(input: EntityCity): City {
        return City(
            title = input.title ?: "",
            desc = input.desc ?: "",
            imageUrl = input.imageUrl
        )
    }
}

class CityListMapper(mapper: CityMapper) : NullableInputListMapperImpl<EntityCity, City>(mapper)