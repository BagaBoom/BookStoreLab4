package com.radchuk.bookstorelab4.model


data class OrderBookResponse(
    val OrderBookId: Long?,
    val customerName: String,
    val isbn: String,
    val quantity: Int,
    val status: String
)
