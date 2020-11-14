package com.pkostrzenski.takemine.api

import com.pkostrzenski.takemine.models.City
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface CitiesApi {

    @GET("/api/cities")
    fun getAllCities(): Deferred<Response<List<City>>>

}