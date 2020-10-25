package com.pkostrzenski.takemine.repository



object MainRepository : RepositoryDao, BaseRepository(){


    override suspend fun authenticate(email: String, password: String): Boolean? {
        return true
    }

    override suspend fun register(email: String, password: String): Boolean? {
        return true
    }
}