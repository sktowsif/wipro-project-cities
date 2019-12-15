package com.example.cities.data

import com.example.cities.BuildConfig
import com.example.cities.di.createWebService
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import retrofit2.await
import retrofit2.mock.Calls
import java.io.IOException

class CountryRepositoryTest {

    lateinit var repository: CountryDataSource

    @Before
    fun setUp() {
        val apiService = createWebService<CountryAPI>(BuildConfig.BASE_API_URL)
        repository = CountryRepository(apiService)

    }

    @Test
    fun testApiCallSuccess() {
        val expectedTitle = "Canada"
        val responseTitle = runBlocking {
            val country = repository.getData()
            country.cities.forEach(::println)
            country.title
        }
        assert(responseTitle.equals(expectedTitle, true))
    }

    @Test
    fun testApiCallFailure() {
        runBlocking {
            val expectedResponse = IOException("Some error occurred")
            `when`(repository.getData()).thenReturn(Calls.failure<Country>(expectedResponse).await())
        }
    }

    @After
    fun tearDown() {

    }
}