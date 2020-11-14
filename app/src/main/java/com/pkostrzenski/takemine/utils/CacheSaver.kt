package com.pkostrzenski.takemine.utils

import com.pkostrzenski.takemine.models.City

interface CacheSaver {
    fun saveToken(token: String)
    fun retrieveToken(): String?
    fun saveCity(city: City)
    fun retrieveCity(): City?
}