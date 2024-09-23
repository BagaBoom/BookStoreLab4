package com.radchuk.bookstorelab4


import com.radchuk.bookstorelab4.entity.Book
import com.radchuk.bookstorelab4.model.BookRequest
import com.radchuk.bookstorelab4.repositores.BookRepository
import com.radchuk.bookstorelab4.services.BookService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.whenever
import java.util.*

class BookServiceTest {

    private val bookRepository = Mockito.mock(BookRepository::class.java)
    private val bookService = BookService(bookRepository)

    @Test
    fun `should add a book`() {
        val bookRequest = BookRequest("12345", "Title", "Author", 10, 19.99)
        val book = Book(isbn = "12345", title = "Title", author = "Author", quantity = 10, price = 19.99)
        whenever(bookRepository.save(Mockito.any(Book::class.java))).thenReturn(book)

        val result = bookService.addBook(bookRequest)
        assertEquals(result.isbn, "12345")
        assertEquals(result.title, "Title")
    }

    @Test
    fun `should get a book`() {
        val book = Book(id = 1L, isbn = "12345", title = "Title", author = "Author", quantity = 10, price = 19.99)
        whenever(bookRepository.findById(1L)).thenReturn(Optional.of(book))

        val result = bookService.getBook(1L)
        assertEquals(result.id, 1L)
        assertEquals(result.isbn, "12345")
    }

    @Test
    fun `should delete a book`() {
        bookService.deleteBook(1L)
        Mockito.verify(bookRepository).deleteById(1L)
    }
}
