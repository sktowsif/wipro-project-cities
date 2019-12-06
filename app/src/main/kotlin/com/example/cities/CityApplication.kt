package com.example.cities

import android.app.Application
import com.example.cities.di.apiModule
import com.example.cities.di.repositoryModule
import com.example.cities.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class CityApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@CityApplication)
            modules(listOf(apiModule, repositoryModule, viewModelModule))
        }
    }

}