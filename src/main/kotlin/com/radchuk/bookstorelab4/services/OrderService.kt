package com.radchuk.bookstorelab4.services


import com.radchuk.bookstorelab4.entity.OrderBook
import com.radchuk.bookstorelab4.model.OrderBookRequest
import com.radchuk.bookstorelab4.model.OrderBookResponse
import com.radchuk.bookstorelab4.model.OrderBookStatusUpdate
import com.radchuk.bookstorelab4.repositores.BookRepository
import com.radchuk.bookstorelab4.repositores.OrderBookRepository
import org.springframework.stereotype.Service

@Service
class OrderBookService(
    private val bookRepository: BookRepository,
    private val OrderBookRepository: OrderBookRepository
) {

    fun createOrderBook(OrderBookRequest: OrderBookRequest): OrderBookResponse {
        val book = bookRepository.findByIsbn(OrderBookRequest.isbn) ?: throw IllegalArgumentException("Book not found")
        if (book.quantity < OrderBookRequest.quantity) {
            throw IllegalArgumentException("Not enough stock")
        }

        book.quantity -= OrderBookRequest.quantity
        bookRepository.save(book)

        val OrderBook = OrderBook(
            customerName = OrderBookRequest.customerName,
            isbn = OrderBookRequest.isbn,
            quantity = OrderBookRequest.quantity
        )
        val savedOrderBook = OrderBookRepository.save(OrderBook)
        return OrderBookResponse(savedOrderBook.OrderBookId, savedOrderBook.customerName, savedOrderBook.isbn, savedOrderBook.quantity, savedOrderBook.status)
    }

    fun getOrderBook(id: Long): OrderBookResponse {
        val OrderBook = OrderBookRepository.findById(id).orElseThrow { IllegalArgumentException("OrderBook not found") }
        return OrderBookResponse(OrderBook.OrderBookId, OrderBook.customerName, OrderBook.isbn, OrderBook.quantity, OrderBook.status)
    }

    fun updateOrderBookStatus(id: Long, statusUpdate: OrderBookStatusUpdate): OrderBookResponse {
        val OrderBook = OrderBookRepository.findById(id).orElseThrow { IllegalArgumentException("OrderBook not found") }
        OrderBook.status = statusUpdate.status
        OrderBookRepository.save(OrderBook)
        return OrderBookResponse(OrderBook.OrderBookId, OrderBook.customerName, OrderBook.isbn, OrderBook.quantity, OrderBook.status)
    }

    fun getOrderBooksByCustomerName(customerName: String): List<OrderBookResponse> {
        return OrderBookRepository.findByCustomerName(customerName).map {
            OrderBookResponse(it.OrderBookId, it.customerName, it.isbn, it.quantity, it.status)
        }
    }
}
