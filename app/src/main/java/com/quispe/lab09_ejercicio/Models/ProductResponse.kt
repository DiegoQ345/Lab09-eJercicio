package com.quispe.lab09_ejercicio.Models

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("products")
    val products: List<ProductModel>
)