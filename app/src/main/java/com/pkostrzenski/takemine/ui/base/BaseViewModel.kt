package com.pkostrzenski.takemine.ui.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pkostrzenski.takemine.repository.MainRepository
import com.pkostrzenski.takemine.repository.RepositoryDao

open class BaseViewModel(application: Application): AndroidViewModel(application) {

    val repository: RepositoryDao = MainRepository


    private val mutableMessage = MutableLiveData<Int>()
    private val mutableUiEnabled = MutableLiveData<Boolean>()

    val message: LiveData<Int> = mutableMessage
    val uiEnabled: LiveData<Boolean> = mutableUiEnabled


    protected fun displayMessage(message: Int){
        mutableMessage.value = message
    }

    protected fun enableUi(){
        mutableUiEnabled.value = true
    }

    protected fun disableUi(){
        mutableUiEnabled.value = false
    }

//    // handler of errors - in case we wanted to check what went wrong on server side (eg. username/email is already taken)
//    private val errorNotifier = MutableLiveData<Int>()
//    fun getErrorNotifier() = errorNotifier as LiveData<Int>
//
//    protected fun extractResponseData(response: com.pkostrzenski.takemine.api.Result<Any>): Any?{
//        return when(response){
//            is com.pkostrzenski.takemine.api.Result.Success -> response.data
//            is com.pkostrzenski.takemine.api.Result.Error -> {
//                errorNotifier.value = response.error?.errorCode
//                null
//            }
//        }
//    }
}