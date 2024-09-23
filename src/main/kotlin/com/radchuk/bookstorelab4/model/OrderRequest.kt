package com.radchuk.bookstorelab4.model


data class OrderBookRequest(
    val isbn: String,
    val quantity: Int,
    val customerName: String
)
