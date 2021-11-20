package com.example.patostore.domain

data class Details(
    val id: String,
    val name: String,
    val attributes: List<Atributes>
)

data class Atributes(
    val name: String,
    val value_name: String
)