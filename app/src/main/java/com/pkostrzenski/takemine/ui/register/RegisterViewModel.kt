package com.pkostrzenski.takemine.ui.register

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pkostrzenski.takemine.R
import com.pkostrzenski.takemine.ui.base.BaseViewModel
import com.pkostrzenski.takemine.utils.EmailValidator
import com.pkostrzenski.takemine.utils.PasswordValidator
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application) : BaseViewModel(application) {

    private val mutableNavigateToIntroSlider = MutableLiveData<Boolean>()

    val navigateToIntroSlider: LiveData<Boolean> = mutableNavigateToIntroSlider


    fun registerClicked(
        email: String, password: String, confirmPassword: String
    ) = viewModelScope.launch {

        validateEmailAndPasswords(email, password, confirmPassword)?.let {
            displayMessage(it)
            return@launch
        }

        disableUi()
        val response = repository.register(email, password)
        when(response){
            true -> mutableNavigateToIntroSlider.value = true
            false -> displayMessage(R.string.email_already_taken)
            null -> displayMessage(R.string.connection_error)
        }
        if(response != true) enableUi()
    }

    private fun validateEmailAndPasswords(email: String, password: String, confirmPassword: String): Int?{
        val validationErrors = arrayListOf(
            validateEmail(email),
            validatePasswords(password, confirmPassword)
        )
        return validationErrors.filterNotNull().firstOrNull()
    }

    private fun validateEmail(email: String): Int? {
        return if(!EmailValidator.validateEmail(email)) R.string.incorrect_email else null
    }

    private fun validatePasswords(password: String, confirmPassword: String): Int? {
        return PasswordValidator.validatePassword(password, confirmPassword)
    }
}