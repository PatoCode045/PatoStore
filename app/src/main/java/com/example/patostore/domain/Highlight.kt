package com.example.patostore.domain

data class Highlight(
    val id: String,
    val position: Int
)

data class HighlightApiResponse(
    val content: List<Highlight>
)