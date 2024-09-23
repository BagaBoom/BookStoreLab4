package com.radchuk.bookstorelab4




import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.radchuk.bookstorelab4.controlleres.BookController
import com.radchuk.bookstorelab4.model.BookRequest
import com.radchuk.bookstorelab4.model.BookResponse
import com.radchuk.bookstorelab4.services.BookService

@WebMvcTest(BookController::class)
class BookControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var bookService: BookService

    private val objectMapper = jacksonObjectMapper()

    @Test
    fun `should add a book`() {
        val bookRequest = BookRequest("12345", "Book Title", "Author Name", 10, 19.99)
        val bookResponse = BookResponse(1L, "12345", "Book Title", "Author Name", 10, 19.99)

        Mockito.`when`(bookService.addBook(bookRequest)).thenReturn(bookResponse)

        mockMvc.perform(post("/books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(bookRequest)))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.isbn").value("12345"))
            .andExpect(jsonPath("$.title").value("Book Title"))
            .andExpect(jsonPath("$.author").value("Author Name"))
    }

    @Test
    fun `should get a book`() {
        val bookResponse = BookResponse(1L, "12345", "Book Title", "Author Name", 10, 19.99)

        Mockito.`when`(bookService.getBook(1L)).thenReturn(bookResponse)

        mockMvc.perform(get("/books/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.isbn").value("12345"))
            .andExpect(jsonPath("$.title").value("Book Title"))
    }

    @Test
    fun `should update a book`() {
        val bookRequest = BookRequest("12345", "Updated Title", "Updated Author", 5, 25.99)
        val updatedBookResponse = BookResponse(1L, "12345", "Updated Title", "Updated Author", 5, 25.99)

        Mockito.`when`(bookService.updateBook(1L, bookRequest)).thenReturn(updatedBookResponse)

        mockMvc.perform(put("/books/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(bookRequest)))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.title").value("Updated Title"))
            .andExpect(jsonPath("$.author").value("Updated Author"))
    }

    @Test
    fun `should delete a book`() {
        mockMvc.perform(delete("/books/1"))
            .andExpect(status().isNoContent)

        Mockito.verify(bookService, Mockito.times(1)).deleteBook(1L)
    }
}
