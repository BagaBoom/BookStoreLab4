package com.radchuk.bookstorelab4



import com.radchuk.bookstorelab4.entity.Book
import com.radchuk.bookstorelab4.entity.OrderBook
import com.radchuk.bookstorelab4.model.OrderBookRequest
import com.radchuk.bookstorelab4.model.OrderBookResponse
import com.radchuk.bookstorelab4.model.OrderBookStatusUpdate
import com.radchuk.bookstorelab4.repositores.BookRepository
import com.radchuk.bookstorelab4.repositores.OrderBookRepository
import com.radchuk.bookstorelab4.services.OrderBookService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.Mockito
import java.util.*

class OrderBookServiceTest {

    private lateinit var bookRepository: BookRepository
    private lateinit var OrderBookRepository: OrderBookRepository
    private lateinit var OrderBookService: OrderBookService

    @BeforeEach
    fun setUp() {
        bookRepository = mock(BookRepository::class.java)
        OrderBookRepository = mock(OrderBookRepository::class.java)
        OrderBookService = OrderBookService(bookRepository, OrderBookRepository)
    }

    @Test
    fun `should create OrderBook successfully`() {
        val OrderBookRequest = OrderBookRequest("12345", 2, "John Doe")
        val book = Book(1L, "12345", "Test Book", "10", 10 ,19.99)
        val OrderBook = OrderBook(1L, "John Doe", "12345", 2, "Pending")

        `when`(bookRepository.findByIsbn("12345")).thenReturn(book)
        `when`(OrderBookRepository.save(any(OrderBook::class.java))).thenReturn(OrderBook)

        val OrderBookResponse = OrderBookService.createOrderBook(OrderBookRequest)

        assertEquals(OrderBookResponse.customerName, "John Doe")
        assertEquals(OrderBookResponse.isbn, "12345")
        assertEquals(OrderBookResponse.quantity, 2)
        assertEquals(OrderBookResponse.status, "Pending")

        verify(bookRepository, times(1)).save(book)
        verify(OrderBookRepository, times(1)).save(any(OrderBook::class.java))
    }

    @Test
    fun `should throw exception when book not found`() {
        val OrderBookRequest = OrderBookRequest("12345", 2, "John Doe")

        `when`(bookRepository.findByIsbn("12345")).thenReturn(null)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            OrderBookService.createOrderBook(OrderBookRequest)
        }

        assertEquals("Book not found", exception.message)
    }

    @Test
    fun `should get OrderBook by ID`() {
        val OrderBook = OrderBook(1L, "John Doe", "12345", 2, "Pending")

        `when`(OrderBookRepository.findById(1L)).thenReturn(Optional.of(OrderBook))

        val OrderBookResponse = OrderBookService.getOrderBook(1L)

        assertEquals(OrderBookResponse.customerName, "John Doe")
        assertEquals(OrderBookResponse.isbn, "12345")
        assertEquals(OrderBookResponse.quantity, 2)
        assertEquals(OrderBookResponse.status, "Pending")

        verify(OrderBookRepository, times(1)).findById(1L)
    }

    @Test
    fun `should throw exception when OrderBook not found`() {
        `when`(OrderBookRepository.findById(1L)).thenReturn(Optional.empty())

        val exception = assertThrows(IllegalArgumentException::class.java) {
            OrderBookService.getOrderBook(1L)
        }

        assertEquals("OrderBook not found", exception.message)
    }

    @Test
    fun `should update OrderBook status`() {
        val OrderBook = OrderBook(1L, "John Doe", "12345", 2, "Pending")
        val statusUpdate = OrderBookStatusUpdate("Shipped")

        `when`(OrderBookRepository.findById(1L)).thenReturn(Optional.of(OrderBook))
        `when`(OrderBookRepository.save(any(OrderBook::class.java))).thenReturn(OrderBook)

        val OrderBookResponse = OrderBookService.updateOrderBookStatus(1L, statusUpdate)

        assertEquals(OrderBookResponse.status, "Shipped")

        verify(OrderBookRepository, times(1)).save(OrderBook)
    }

    @Test
    fun `should return OrderBooks by customer name`() {
        val OrderBook1 = OrderBook(1L, "John Doe", "12345", 2, "Pending")
        val OrderBook2 = OrderBook(2L, "John Doe", "67890", 1, "Shipped")

        `when`(OrderBookRepository.findByCustomerName("John Doe")).thenReturn(listOf(OrderBook1, OrderBook2))

        val OrderBooks = OrderBookService.getOrderBooksByCustomerName("John Doe")

        assertEquals(2, OrderBooks.size)
        assertEquals("12345", OrderBooks[0].isbn)
        assertEquals("67890", OrderBooks[1].isbn)

        verify(OrderBookRepository, times(1)).findByCustomerName("John Doe")
    }
}

