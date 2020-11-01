package com.pkostrzenski.takemine.ui.login

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pkostrzenski.takemine.R
import com.pkostrzenski.takemine.ui.base.BaseViewModel
import com.pkostrzenski.takemine.utils.EmailValidator
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : BaseViewModel(application) {

    private val mutableMessage = MutableLiveData<Int>()
    private val mutableNavigateToLogged = MutableLiveData<Boolean>().apply { value = false }
    private val mutableNavigateToRegister = MutableLiveData<Boolean>().apply { value = false }

    val navigateToLogged: LiveData<Boolean> = mutableNavigateToLogged
    val navigateToRegister: LiveData<Boolean> = mutableNavigateToRegister


    fun authenticateUser(email: String, password: String) = viewModelScope.launch {
        if(!EmailValidator.validateEmail(email) || password.isBlank()){
            mutableMessage.value = R.string.provide_correct_email_and_password
            return@launch
        }

        disableUi()
        val response = repository.authenticate(email, password)
        when(response){
            true -> mutableNavigateToLogged.value = true
            false -> displayMessage(R.string.provide_correct_email_and_password)
            null -> displayMessage(R.string.connection_error)
        }
        if(response != true) enableUi()
    }

    fun registerClicked(){
        mutableNavigateToRegister.value = true
    }
}