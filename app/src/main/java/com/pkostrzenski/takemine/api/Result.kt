package com.pkostrzenski.takemine.api

sealed class Result<out T: Any> {
    data class Success<out T: Any>(val data: T): Result<T>()
    data class Error(val error: ErrorResponse? = null): Result<Nothing>()
}