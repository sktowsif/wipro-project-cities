package com.example.cities.vm

import android.content.Context
import androidx.lifecycle.*
import com.example.cities.base.Outcome
import com.example.cities.data.Country
import com.example.cities.data.CountryDataSource
import com.example.cities.ext.emitFailure
import com.example.cities.ext.emitLoading
import com.example.cities.ext.emitSuccess
import com.example.cities.support.ConnectivityLiveData
import kotlinx.coroutines.Dispatchers

class HomeViewModel(context: Context, private val dataSource: CountryDataSource) :
    ViewModel() {

    private val fetchCountryData = MutableLiveData<Boolean>()

    val connectivityLiveData = ConnectivityLiveData(context)

    init {
        fetchData()
    }

    val countryLiveData = fetchCountryData.switchMap {
        liveData<Outcome<Country>>(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            emitLoading()
            try {
                emitSuccess(dataSource.getData())
            } catch (ex: Exception) {
                ex.printStackTrace()
                emitFailure(ex)
            }
        }
    }

    fun fetchData() {
        fetchCountryData.value = true
    }
}