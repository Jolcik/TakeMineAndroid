package com.pkostrzenski.takemine.ui.city_chooser

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pkostrzenski.takemine.R
import com.pkostrzenski.takemine.models.City
import com.pkostrzenski.takemine.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class CityChooserViewModel(application: Application): BaseViewModel(application) {

    private lateinit var allCities: List<City>

    private val mutableCities = MutableLiveData<List<City>>()
    private val mutableNavigateToMain = MutableLiveData<Boolean>()
    private val mutableLoadingVisible = MutableLiveData<Boolean>()

    val cities: LiveData<List<City>> = mutableCities
    val navigateToMain: LiveData<Boolean> = mutableNavigateToMain
    val loadingVisible: LiveData<Boolean> = mutableLoadingVisible

    init {
        fetchCities()
    }

    private fun fetchCities() = viewModelScope.launch {
        mutableLoadingVisible.value = true

        repository.getAllCities()?.let {
            mutableCities.value = it
            allCities = it
        } ?: run {
            displayMessage(R.string.connection_error)
            disableUi()
        }

        mutableLoadingVisible.value = false
    }

    fun chooseCity(city: City){
        repository.setCurrentCity(city)
        mutableNavigateToMain.value = true
    }

    fun filterCitiesBySubname(subname: String){
        mutableCities.value = allCities.filter {
            it.name.contains(subname, ignoreCase = false)
        }
    }

}