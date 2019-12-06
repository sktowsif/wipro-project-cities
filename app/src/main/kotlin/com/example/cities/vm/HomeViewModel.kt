package com.example.cities.vm

import androidx.lifecycle.*
import com.example.cities.base.Outcome
import com.example.cities.data.CountryDataSource
import com.example.cities.data.EntityCountry
import com.example.cities.ext.emitFailure
import com.example.cities.ext.emitLoading
import com.example.cities.ext.emitSuccess
import kotlinx.coroutines.Dispatchers

class HomeViewModel(private val dataSource: CountryDataSource) : ViewModel() {

    private val fetchCountryData = MutableLiveData<Boolean>()

    val countryLiveData = fetchCountryData.switchMap {
        liveData<Outcome<EntityCountry>>(context = viewModelScope.coroutineContext + Dispatchers.IO) {
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