package com.quispe.lab09_ejercicio.Interfaz

import com.quispe.lab09_ejercicio.Models.ProductModel
import com.quispe.lab09_ejercicio.Models.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT

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

    @POST("products/add")
    suspend fun addProduct(@Body producto: ProductModel): ProductModel

    @PUT("products/{id}")
    suspend fun updateProduct(@Path("id") id: Int, @Body producto: ProductModel): ProductModel

    @DELETE("products/{id}")
    suspend fun deleteProduct(@Path("id") id: Int): Unit
}


