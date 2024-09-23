package com.radchuk.bookstorelab4.entity


import jakarta.persistence.*

@Entity
data class OrderBook(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val OrderBookId: Long? = null,
    val customerName: String,
    val isbn: String,
    val quantity: Int,
    var status: String = "Pending"
) {
    constructor() : this(0, "", "", 0) {

    }
}
