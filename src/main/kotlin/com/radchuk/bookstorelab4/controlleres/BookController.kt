package com.radchuk.bookstorelab4.controlleres

import com.radchuk.bookstorelab4.model.BookRequest
import com.radchuk.bookstorelab4.model.BookResponse
import com.radchuk.bookstorelab4.services.BookService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/books")
class BookController(private val bookService: BookService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addBook(@RequestBody bookRequest: BookRequest): BookResponse {
        return bookService.addBook(bookRequest)
    }

    @PutMapping("/{id}")
    fun updateBook(@PathVariable id: Long, @RequestBody bookRequest: BookRequest): BookResponse {
        return bookService.updateBook(id, bookRequest)
    }

    @GetMapping("/{id}")
    fun getBook(@PathVariable id: Long): BookResponse {
        return bookService.getBook(id)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBook(@PathVariable id: Long) {
        bookService.deleteBook(id)
    }
}

