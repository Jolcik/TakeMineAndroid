package com.pkostrzenski.takemine.ui.add_notifier

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pkostrzenski.takemine.R
import com.pkostrzenski.takemine.models.Category
import com.pkostrzenski.takemine.models.ItemType
import com.pkostrzenski.takemine.models.Location
import com.pkostrzenski.takemine.models.Product
import com.pkostrzenski.takemine.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class AddNotifierViewModel(application: Application) : BaseViewModel(application) {

    private val mutableCategories = MutableLiveData<List<Category>>()
    private val mutableItemTypes = MutableLiveData<List<ItemType>>()
    private val mutableLocations = MutableLiveData<Set<Location>>()
    private val mutableNavigateToLocationPicker = MutableLiveData<Boolean>()
    private val mutableNavigateToMain = MutableLiveData<Boolean>()

    val categories: LiveData<List<Category>> = mutableCategories
    val itemTypes: LiveData<List<ItemType>> = mutableItemTypes
    val locations: LiveData<Set<Location>> = mutableLocations
    val navigateToLocationPicker: LiveData<Boolean> = mutableNavigateToLocationPicker
    val navigateToMain: LiveData<Boolean> = mutableNavigateToMain

    var chosenItemType: ItemType? = null

    init {
        repository.clearLocations()

        viewModelScope.launch {
            disableUi()
            when (val response = repository.getCategories()) {
                null -> displayMessage(R.string.connection_error)
                else -> mutableCategories.value = response
            }
            enableUi()
        }
    }

    fun categoryPicked(category: Category) {
        viewModelScope.launch {
            disableUi()
            when (val response = repository.getItemTypes(category)) {
                null -> displayMessage(R.string.connection_error)
                else -> mutableItemTypes.value = response
            }
            enableUi()
        }
    }

    fun itemTypePicked(itemType: ItemType) {
        chosenItemType = itemType
    }

    fun pickLocationClicked() {
        mutableNavigateToLocationPicker.value = true
    }

    fun gotActivityOkResult() {
        mutableLocations.value = repository.locations
    }

    fun addNotifierClicked() {
        if (locations.value == null || chosenItemType == null)
            return

        viewModelScope.launch {
            disableUi()
            when(repository.addNotifier(locations.value!!, chosenItemType!!)) {
                null -> {
                    displayMessage(R.string.connection_error)
                    enableUi()
                }
                else -> {
                    displayMessage(R.string.successfully_added_product)
                    mutableNavigateToMain.value = true
                }
            }
        }
    }
}