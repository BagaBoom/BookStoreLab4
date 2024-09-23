package com.radchuk.bookstorelab4

import com.radchuk.bookstorelab4.controlleres.OrderBookController
import com.radchuk.bookstorelab4.model.OrderBookRequest
import com.radchuk.bookstorelab4.model.OrderBookResponse
import com.radchuk.bookstorelab4.model.OrderBookStatusUpdate
import com.radchuk.bookstorelab4.services.OrderBookService
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

@WebMvcTest(OrderBookController::class)
class OrderBookControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var OrderBookService: OrderBookService

    private val objectMapper = jacksonObjectMapper()

    @Test
    fun `should create an OrderBook`() {
        val OrderBookRequest = OrderBookRequest("12345", 2, "John Doe")
        val OrderBookResponse = OrderBookResponse(1L, "John Doe", "12345", 2, "Pending")

        Mockito.`when`(OrderBookService.createOrderBook(OrderBookRequest)).thenReturn(OrderBookResponse)

        mockMvc.perform(post("/OrderBooks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(OrderBookRequest)))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.isbn").value("12345"))
            .andExpect(jsonPath("$.customerName").value("John Doe"))
            .andExpect(jsonPath("$.quantity").value(2))
    }

    @Test
    fun `should get an OrderBook`() {
        val OrderBookResponse = OrderBookResponse(1L, "John Doe", "12345", 2, "Pending")

        Mockito.`when`(OrderBookService.getOrderBook(1L)).thenReturn(OrderBookResponse)

        mockMvc.perform(get("/OrderBooks/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.customerName").value("John Doe"))
            .andExpect(jsonPath("$.isbn").value("12345"))
    }

    @Test
    fun `should update OrderBook status`() {
        val statusUpdate = OrderBookStatusUpdate("Shipped")
        val updatedOrderBookResponse = OrderBookResponse(1L, "John Doe", "12345", 2, "Shipped")

        Mockito.`when`(OrderBookService.updateOrderBookStatus(1L, statusUpdate)).thenReturn(updatedOrderBookResponse)

        mockMvc.perform(put("/OrderBooks/1/status")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(statusUpdate)))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.status").value("Shipped"))
    }

    @Test
    fun `should get OrderBooks by customer name`() {
        val OrderBooks = listOf(
            OrderBookResponse(1L, "John Doe", "12345", 2, "Pending"),
            OrderBookResponse(2L, "John Doe", "67890", 1, "Shipped")
        )

        Mockito.`when`(OrderBookService.getOrderBooksByCustomerName("John Doe")).thenReturn(OrderBooks)

        mockMvc.perform(get("/OrderBooks/customer/John Doe"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].isbn").value("12345"))
            .andExpect(jsonPath("$[1].isbn").value("67890"))
    }
}
