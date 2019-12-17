package com.example.cities.vm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.example.cities.base.Outcome
import com.example.cities.data.City
import com.example.cities.data.Country
import com.example.cities.data.CountryDataSource
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.timeout
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okio.IOException
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.`when`

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel
    private lateinit var repository: CountryDataSource
    private lateinit var dataObserver: Observer<Outcome<Country>>

    private val errorOutcome = Outcome.failure<Throwable>(IOException("Network connection failure"))

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @ObsoleteCoroutinesApi
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        repository = mock()

        viewModel = HomeViewModel(getApplicationContext(), repository)
        dataObserver = mock()
    }

    @Test
    fun testApiCallSuccess() {
        runBlocking {
            val expectedResponse = Country(
                title = "India",
                cities = arrayListOf(City(title = "Kolkata", desc = "City of Joy", imageUrl = null))
            )

            `when`(repository.getData()).thenReturn(expectedResponse)

            viewModel.countryLiveData.observeForever(dataObserver)
            viewModel.fetchData()

            delay(500L)
            verify(dataObserver, timeout(30000L)).onChanged(Outcome.success(expectedResponse))
        }
    }

    @Test
    fun testApiCallFailure() {
        runBlocking {
            val expectedResponse = NullPointerException("Title not provided")

            `when`(repository.getData()).thenThrow(expectedResponse)

            viewModel.countryLiveData.observeForever(dataObserver)
            viewModel.fetchData()

            delay(500L)
            verify(dataObserver, timeout(30000L)).onChanged(Outcome.failure(expectedResponse))
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }
}