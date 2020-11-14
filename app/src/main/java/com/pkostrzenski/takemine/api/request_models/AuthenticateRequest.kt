package com.pkostrzenski.takemine.api.request_models

data class AuthenticateRequest(
    val username: String,
    val password: String
)