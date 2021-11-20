package com.example.patostore.domain

data class Product(
    val id: String,
    val title: String,
    val thumbnail: String,
    var position: Int
)

data class ProductApiResponse(
    val status: Int,
    val body: Product
)
