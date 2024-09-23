package com.radchuk.bookstorelab4.model


data class BookResponse(
    val id: Long?,
    val isbn: String,
    val title: String,
    val author: String,
    val quantity: Int,
    val price: Double
)
