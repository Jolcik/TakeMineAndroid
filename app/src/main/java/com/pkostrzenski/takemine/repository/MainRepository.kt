package com.pkostrzenski.takemine.repository

import com.pkostrzenski.takemine.api.ApiFactory
import com.pkostrzenski.takemine.api.Result
import com.pkostrzenski.takemine.api.request_models.AuthenticateRequest
import com.pkostrzenski.takemine.api.request_models.RegisterRequest
import com.pkostrzenski.takemine.api.request_models.RegisterResponse
import com.pkostrzenski.takemine.models.City
import com.pkostrzenski.takemine.models.Picture
import com.pkostrzenski.takemine.models.Product
import com.pkostrzenski.takemine.models.User
import com.pkostrzenski.takemine.utils.CacheSaver
import com.pkostrzenski.takemine.utils.SharedPreferencesSaver
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody


object MainRepository : RepositoryDao, BaseRepository(){

    private val apiFactory = ApiFactory
    private val usersApi = apiFactory.usersApi
    private val citiesApi = apiFactory.citiesApi
    private val productsApi = apiFactory.productsApi

    private var userId: Int? = null
    private var currentCity: City? = null

    private val saver: CacheSaver = SharedPreferencesSaver

    init {
        currentCity = saver.retrieveCity()
    }


    override suspend fun authenticate(email: String, password: String): Boolean? {
        return safeApiCallWithError { usersApi.authenticate(
            AuthenticateRequest(email, password)
        ).await() }?.let {
            when(it){
                is Result.Success -> {
                    apiFactory.token = it.data.token
                    userId = it.data.id
                    saver.saveToken(it.data.token)
                    true
                }
                is Result.Error -> false
            }
        }
    }

    override suspend fun register(email: String, password: String): Boolean? {
        return safeApiCallWithError {
            usersApi.register(RegisterRequest(email, password)).await()
        }?.let {
            when(it){
                is Result.Success -> {
                    apiFactory.token = it.data.token
                    userId = it.data.id
                    saver.saveToken(it.data.token)
                    true
                }
                is Result.Error -> false
            }
        }
    }

    override suspend fun getUserInfo(): User? {
        return userId?.let {
            safeApiCall {
                usersApi.getUserInfo(it).await()
            }
        }
    }

    override fun getCurrentCity() = currentCity

    override fun setCurrentCity(city: City) {
        currentCity = city
        saver.saveCity(city)
    }

    override suspend fun getAllCities(): List<City>? {
        return safeApiCall { citiesApi.getAllCities().await() }
    }

    override suspend fun getProductsFromCity(): List<Product>? {
        return currentCity?.let{
            safeApiCall {
                productsApi.getProductsByCityId(currentCity!!.id).await()
            }
        }
    }

    override suspend fun uploadPhoto(photo: ByteArray): Picture? {
        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), photo)
        val body = MultipartBody.Part.createFormData("file", "picture.jpg", requestFile)

        return safeApiCall {
            productsApi.uploadPicture(body).await()
        }
    }
}