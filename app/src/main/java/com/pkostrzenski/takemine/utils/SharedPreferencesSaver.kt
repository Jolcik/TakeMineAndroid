package com.pkostrzenski.takemine.utils

import android.content.SharedPreferences
import com.google.gson.Gson
import com.pkostrzenski.takemine.TakeMineApplication
import com.pkostrzenski.takemine.models.City

object SharedPreferencesSaver: CacheSaver {

    private const val PRIVATE_MODE = 0
    private const val PREF_NAME = "mobile-toilets-cache"

    private const val TOKEN_KEY = "token"
    private const val CITY_KEY = "city"

    private val gson = Gson()
    private val sharedPreferences: SharedPreferences

    init {
        sharedPreferences = TakeMineApplication.getAppContext().getSharedPreferences(
            PREF_NAME, PRIVATE_MODE
        )
    }

    override fun saveToken(token: String) = save(token, TOKEN_KEY)

    override fun retrieveToken(): String? = retrieve(TOKEN_KEY, String::class.java) as String?

    override fun saveCity(city: City) = save(city, CITY_KEY)

    override fun retrieveCity(): City? = retrieve(CITY_KEY, City::class.java) as City?


    private fun save(objectToSave: Any, key: String){
        sharedPreferences
            .edit()
            .putString(key, gson.toJson(objectToSave))
            .apply()
    }

    private fun retrieve(key: String, objectClass: Class<*>): Any? {
        return sharedPreferences.getString(key, null)?.let {
            gson.fromJson(it, objectClass)
        }

    }

    fun clear() = sharedPreferences.edit().clear().apply()

}