package com.pkostrzenski.takemine.repository

import com.pkostrzenski.takemine.models.City
import com.pkostrzenski.takemine.models.Product
import com.pkostrzenski.takemine.models.User


interface RepositoryDao {
    suspend fun authenticate(email: String, password: String): Boolean?
    suspend fun register(email: String, password: String): Boolean?
    suspend fun getUserInfo(): User?
    fun getCurrentCity(): City?
    fun setCurrentCity(city: City)
    suspend fun getAllCities(): List<City>?
    suspend fun getProductsFromCity(): List<Product>?
}