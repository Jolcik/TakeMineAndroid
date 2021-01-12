package com.pkostrzenski.takemine.ui.post_product

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pkostrzenski.takemine.R
import com.pkostrzenski.takemine.models.*
import com.pkostrzenski.takemine.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class PostProductViewModel (application: Application): BaseViewModel(application) {


    private val mutableCategories = MutableLiveData<List<Category>>()
    private val mutableItemTypes = MutableLiveData<List<ItemType>>()
    private val mutablePictures = MutableLiveData<Set<Picture>>()
    private val mutableLocations = MutableLiveData<Set<Location>>()
    private val mutableNavigateToPhotoPicker = MutableLiveData<Boolean>()
    private val mutableNavigateToLocationPicker = MutableLiveData<Boolean>()
    private val mutableNavigateToMain = MutableLiveData<Boolean>()

    val categories: LiveData<List<Category>> = mutableCategories
    val itemTypes: LiveData<List<ItemType>> = mutableItemTypes
    val pictures: LiveData<Set<Picture>> = mutablePictures
    val locations: LiveData<Set<Location>> = mutableLocations
    val navigateToLocationPicker: LiveData<Boolean> = mutableNavigateToLocationPicker
    val navigateToPhotoPicker: LiveData<Boolean> = mutableNavigateToPhotoPicker
    val navigateToMain: LiveData<Boolean> = mutableNavigateToMain

    var chosenItemType: ItemType? = null

    init {
        mutablePictures.value = setOf()
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

    fun uploadPhotoClicked() {
        mutableNavigateToPhotoPicker.value = true
    }

    fun uploadPhoto(photo: ByteArray) {
        viewModelScope.launch {
            disableUi()

            when(val response = repository.uploadPhoto(photo)) {
                null -> displayMessage(R.string.connection_error)
                else -> mutablePictures.value = mutablePictures.value?.plus(response)
            }

            enableUi()
        }
    }

    fun gotActivityOkResult() {
        mutableLocations.value = repository.locations
    }

    fun postProductClicked(name: String, description: String) {
        val product = Product(
            id = null,
            name = name,
            description = description,
            city = repository.getCurrentCity()!!,
            locations = locations.value!!,
            itemType = chosenItemType!!,
            pictures = pictures.value!!,
            buyer = null
        )

        viewModelScope.launch {
            disableUi()
            when(repository.postProduct(product)) {
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
