package com.pkostrzenski.takemine.utils

object EmailValidator {

    fun validateEmail(email: String): Boolean{
        val regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$".toRegex()
        return email.matches(regex)
    }

}