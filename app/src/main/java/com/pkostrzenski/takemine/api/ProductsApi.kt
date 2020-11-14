package com.pkostrzenski.takemine.api

import com.pkostrzenski.takemine.models.Category
import com.pkostrzenski.takemine.models.Product
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ProductsApi {

    @GET("/api/products/{productId}")
    fun getProductById(
        @Path("productId") productId: Int
    ): Deferred<Response<Product>>


    @GET("/api/products/from-city/{cityId}")
    fun getProductsByCityId(
        @Path("cityId") cityId: Int
    ): Deferred<Response<List<Product>>>


    @GET("/api/categories")
    fun getAllCategories(): Deferred<Response<List<Category>>>


    @GET("/api/categories/{categoryId}/item-types")
    fun getItemTypesByCategoryId(
        @Path("categoryId") categoryId: Int
    ): Deferred<Response<List<Category>>>


    @POST("/api/products")
    fun postProduct(
        @Body body: Product
    ): Deferred<Response<Product>>


    @POST("/api/products/{productId}/buy")
    fun buyProduct(): Deferred<Response<Boolean>>
}