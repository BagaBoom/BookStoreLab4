package com.radchuk.bookstorelab4.entity


import jakarta.persistence.*

@Entity
data class Book(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(unique = true)
    val isbn: String,
    var title: String,
    var author: String,
    var quantity: Int,
    val price: Double
) {
    constructor() : this(0 , "", "", "", 0, 0.0) {

    }
}

