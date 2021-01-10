package com.pkostrzenski.takemine.ui.post_product

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pkostrzenski.takemine.R
import com.pkostrzenski.takemine.models.Picture
import com.pkostrzenski.takemine.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class PostProductViewModel (application: Application): BaseViewModel(application) {

    private val mutablePictures = MutableLiveData<List<Picture>>()
    private val mutableNavigateToPhotoPicker = MutableLiveData<Boolean>()
    private val mutableNavigateToMain = MutableLiveData<Int>()

    val pictures: LiveData<List<Picture>> = mutablePictures
    val navigateToPhotoPicker: LiveData<Boolean> = mutableNavigateToPhotoPicker
    val navigateToMain: LiveData<Int> = mutableNavigateToMain

    init {
        mutablePictures.value = listOf()
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

    fun postProductClicked() {

    }

}
