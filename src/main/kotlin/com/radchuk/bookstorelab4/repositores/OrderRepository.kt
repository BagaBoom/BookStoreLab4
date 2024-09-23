package com.radchuk.bookstorelab4.repositores

import com.radchuk.bookstorelab4.entity.OrderBook
import org.springframework.data.jpa.repository.JpaRepository

interface OrderBookRepository : JpaRepository<OrderBook, Long> {
    fun findByCustomerName(customerName: String): List<OrderBook>
}