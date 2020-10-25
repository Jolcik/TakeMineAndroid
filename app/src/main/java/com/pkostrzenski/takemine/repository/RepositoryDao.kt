package com.pkostrzenski.takemine.repository


interface RepositoryDao {
    suspend fun authenticate(email: String, password: String): Boolean?
    suspend fun register(email: String, password: String): Boolean?
}