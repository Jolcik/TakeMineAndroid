package com.pkostrzenski.takemine.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiFactory {

    private const val URL = "http://192.168.8.126:8080"

    var token: String? = null

    private val okHttpClient = OkHttpClient()
        .newBuilder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
            token?.run { request.addHeader("Authorization", "Bearer $token") } // add Bearer token if we have one

            chain.proceed(request.build())
        }
        .build()

    fun retrofit(): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val usersApi = retrofit().create(UsersApi::class.java)
    val citiesApi = retrofit().create(CitiesApi::class.java)
    val productsApi = retrofit().create(ProductsApi::class.java)
}