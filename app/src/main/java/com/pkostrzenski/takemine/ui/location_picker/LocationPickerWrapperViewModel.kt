package com.pkostrzenski.takemine.ui.location_picker

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pkostrzenski.takemine.R
import com.pkostrzenski.takemine.models.City
import com.pkostrzenski.takemine.models.Location
import com.pkostrzenski.takemine.ui.base.BaseViewModel

class LocationPickerWrapperViewModel(application: Application) : BaseViewModel(application) {

    private val mutableNavigateToMain = MutableLiveData<Boolean>()
    private val mutableNavigateToLocationPicker = MutableLiveData<Boolean>()
    private val mutableAddress = MutableLiveData<String>()
    private val mutableFromHour = MutableLiveData<String>()
    private val mutableToHour = MutableLiveData<String>()

    val navigateToMain: LiveData<Boolean> = mutableNavigateToMain
    val navigateToLocationPicker: LiveData<Boolean> = mutableNavigateToLocationPicker
    val address: LiveData<String> = mutableAddress
    val fromHour: LiveData<String> = mutableFromHour
    val toHour: LiveData<String> = mutableToHour

    val city: City? = repository.getCurrentCity()

    private var lat: Double? = null
    private var lng: Double? = null

    fun pickCoordinatesClicked() {
        mutableNavigateToLocationPicker.value = true
    }

    fun coordinatesReceived(lat: Double, lng: Double, address: String) {
        this.lat = lat
        this.lng = lng
        mutableAddress.value = address
    }

    fun fromHourSet(fromHour: String) {
        mutableFromHour.value = fromHour
    }

    fun toHourSet(toHour: String) {
        mutableToHour.value = toHour
    }

    fun submitLocationClicked(monday: Boolean, tuesday: Boolean, wednesday: Boolean, thursday: Boolean, friday: Boolean, saturday: Boolean, sunday: Boolean) {
        if (address.value == null) {
            displayMessage(R.string.no_location_provided)
            return
        }
        
        if (fromHour.value == null || toHour.value == null) {
            displayMessage(R.string.no_hours_provided)
            return
        }

        if (!listOf(monday, tuesday, wednesday, thursday, friday, saturday).any{ it }) {
            displayMessage(R.string.no_day_chosen)
            return
        }

        repository.addLocation(
            Location(
                name = address.value!!,
                lat = lat!!,
                lng = lng!!,
                fromHour = fromHour.value!!,
                toHour = toHour.value!!,
                monday = monday,
                tuesday = tuesday,
                wednesday = wednesday,
                thursday = thursday,
                friday = friday,
                saturday = saturday,
                sunday = sunday
            )
        )
        Log.d("LOC", repository.locations.toString())
        displayMessage(R.string.successfully_added_location)
        mutableNavigateToMain.value = true
    }

}