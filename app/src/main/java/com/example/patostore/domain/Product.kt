package com.example.patostore.domain

data class Product(
    val id: String,
    val title: String,
    val secure_thumbnail: String,
    var position: Int,
    val catalog_product_id: String
)

data class ProductApiResponse(
    val status: Int,
    val body: Product
)
