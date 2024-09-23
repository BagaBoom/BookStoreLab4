package com.radchuk.bookstorelab4.services


import com.radchuk.bookstorelab4.entity.Book
import com.radchuk.bookstorelab4.model.BookRequest
import com.radchuk.bookstorelab4.model.BookResponse
import com.radchuk.bookstorelab4.repositores.BookRepository
import org.springframework.stereotype.Service

@Service
class BookService(private val bookRepository: BookRepository) {

    fun addBook(bookRequest: BookRequest): BookResponse {
        if (bookRepository.findByIsbn(bookRequest.isbn) != null) {
            throw IllegalArgumentException("Book with this ISBN already exists")
        }
        val book = Book(
            isbn = bookRequest.isbn,
            title = bookRequest.title,
            author = bookRequest.author,
            quantity = bookRequest.quantity,
            price = bookRequest.price
        )
        val savedBook = bookRepository.save(book)
        return BookResponse(savedBook.id, savedBook.isbn, savedBook.title, savedBook.author, savedBook.quantity, savedBook.price)
    }

    fun updateBook(id: Long, bookRequest: BookRequest): BookResponse {
        val existingBook = bookRepository.findById(id).orElseThrow { IllegalArgumentException("Book not found") }
        if (existingBook.isbn != bookRequest.isbn) {
            throw IllegalArgumentException("Cannot update ISBN")
        }
        existingBook.apply {
            title = bookRequest.title
            author = bookRequest.author
            quantity = bookRequest.quantity
        }
        bookRepository.save(existingBook)
        return BookResponse(existingBook.id, existingBook.isbn, existingBook.title, existingBook.author, existingBook.quantity, existingBook.price)
    }

    fun getBook(id: Long): BookResponse {
        val book = bookRepository.findById(id).orElseThrow { IllegalArgumentException("Book not found") }
        return BookResponse(book.id, book.isbn, book.title, book.author, book.quantity, book.price)
    }

    fun deleteBook(id: Long) {
        bookRepository.deleteById(id)
    }
}

