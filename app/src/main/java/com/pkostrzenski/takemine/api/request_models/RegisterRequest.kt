package com.pkostrzenski.takemine.api.request_models

data class RegisterRequest(
    val email: String,
    val password: String
)