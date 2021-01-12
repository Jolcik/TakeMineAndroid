package com.pkostrzenski.takemine.repository

import com.pkostrzenski.takemine.models.*


interface RepositoryDao {

    var locations: Set<Location>

    suspend fun authenticate(email: String, password: String): Boolean?
    suspend fun register(email: String, password: String): Boolean?
    suspend fun getUserInfo(): User?
    fun getCurrentCity(): City?
    fun setCurrentCity(city: City)
    suspend fun getAllCities(): List<City>?
    suspend fun getProductsFromCity(): List<Product>?
    suspend fun uploadPhoto(photo: ByteArray): Picture?
    fun clearLocations()
    fun addLocation(location: Location)
    suspend fun getCategories(): List<Category>?
    suspend fun getItemTypes(category: Category): List<ItemType>?
    suspend fun postProduct(product: Product): Product?
}