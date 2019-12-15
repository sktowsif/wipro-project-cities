package com.example.cities.di

import com.example.cities.BuildConfig
import com.example.cities.data.*
import com.example.cities.vm.HomeViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val testModules = module {
    single<CountryAPI> { createWebService(BuildConfig.BASE_API_URL) }
    single<CountryDataSource> { CountryRepository(get()) }
}

val apiModule = module {
    single<CountryAPI> { createWebService(BuildConfig.BASE_API_URL) }
}

val repositoryModule = module {
    single<CountryDataSource> { CountryRepository(get()) }
}

val dtoMapperModule = module {
    single { CityMapper() }
    single { CityListMapper(get()) }
    single { CountryMapper(get()) }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get(), get()) }
}

inline fun <reified T> createWebService(url: String): T {
    val okHttpClient = OkHttpClient.Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())

    return retrofit.build().create(T::class.java)
}