package com.quispe.lab09_ejercicio.Interfaz

import com.quispe.lab09_ejercicio.Models.ProductModel
import com.quispe.lab09_ejercicio.Models.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApiService {
    @GET("products")
    suspend fun getProducts(): ProductResponse

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): ProductModel

    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): ProductResponse
}


