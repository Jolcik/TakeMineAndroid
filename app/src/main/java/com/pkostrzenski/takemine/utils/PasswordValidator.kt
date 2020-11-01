package com.pkostrzenski.takemine.utils

import com.pkostrzenski.takemine.R


object PasswordValidator {

    fun validatePassword(password: String, confirmPassword: String): Int? {

        if(password.isBlank() || confirmPassword.isBlank())
            return R.string.invalid_password_blank

        if(password != confirmPassword)
            return R.string.invalid_password_different

        if(password.length < 8)
            return R.string.invalid_password_too_short

        return null
    }
}