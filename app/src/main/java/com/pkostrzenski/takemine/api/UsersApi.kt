package com.pkostrzenski.takemine.api

import com.pkostrzenski.takemine.api.request_models.AuthenticateRequest
import com.pkostrzenski.takemine.api.request_models.AuthenticateResponse
import com.pkostrzenski.takemine.api.request_models.RegisterRequest
import com.pkostrzenski.takemine.api.request_models.RegisterResponse
import com.pkostrzenski.takemine.models.User
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UsersApi {

    @POST("/api/authenticate")
    fun authenticate(
        @Body body: AuthenticateRequest
    ): Deferred<Response<AuthenticateResponse>>

    @POST("/api/register")
    fun register(
        @Body body: RegisterRequest
    ): Deferred<Response<RegisterResponse>>

    @GET("/api/users/{userId}")
    fun getUserInfo(
        @Path("userId") userId: Int
    ): Deferred<Response<User>>
}