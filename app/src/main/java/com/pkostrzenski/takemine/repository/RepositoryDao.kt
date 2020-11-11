package com.pkostrzenski.takemine.repository

import com.pkostrzenski.takemine.models.City


interface RepositoryDao {
    suspend fun authenticate(email: String, password: String): Boolean?
    suspend fun register(email: String, password: String): Boolean?
    fun getCurrentCity(): City?
    fun setCurrentCity(city: City)
    suspend fun getAllCities(): List<City>?
}