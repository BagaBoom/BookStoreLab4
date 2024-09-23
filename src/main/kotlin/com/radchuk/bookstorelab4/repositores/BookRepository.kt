package com.radchuk.bookstorelab4.repositores


import com.radchuk.bookstorelab4.entity.Book
import org.springframework.data.jpa.repository.JpaRepository

interface BookRepository : JpaRepository<Book, Long> {
    fun findByIsbn(isbn: String): Book?
}
