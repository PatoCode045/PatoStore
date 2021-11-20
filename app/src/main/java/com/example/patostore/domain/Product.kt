package com.example.patostore.domain

data class Product(
    val id: String,
    val title: String,
    val secure_thumbnail: String,
    var position: Int,
    val catalog_product_id: String,
    val price: Int,
    val currency_id: String,
    val pictures: List<Picture>
)

data class Picture(
    val secure_url: String
)

data class ProductApiResponse(
    val status: Int,
    val body: Product
)