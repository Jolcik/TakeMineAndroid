package com.pkostrzenski.takemine.api.request_models

data class RegisterResponse(
    val id: Int,
    val username: String,
    val token: String
)