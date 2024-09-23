package com.radchuk.bookstorelab4.controlleres


import com.radchuk.bookstorelab4.model.OrderBookRequest
import com.radchuk.bookstorelab4.model.OrderBookResponse
import com.radchuk.bookstorelab4.model.OrderBookStatusUpdate
import com.radchuk.bookstorelab4.services.OrderBookService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/orders")
class OrderBookController(private val OrderBookService: OrderBookService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createOrderBook(@RequestBody OrderBookRequest: OrderBookRequest): OrderBookResponse {
        return OrderBookService.createOrderBook(OrderBookRequest)
    }

    @GetMapping("/{id}")
    fun getOrderBook(@PathVariable id: Long): OrderBookResponse {
        return OrderBookService.getOrderBook(id)
    }

    @PutMapping("/{id}/status")
    fun updateOrderBookStatus(@PathVariable id: Long, @RequestBody statusUpdate: OrderBookStatusUpdate): OrderBookResponse {
        return OrderBookService.updateOrderBookStatus(id, statusUpdate)
    }

    @GetMapping("/customer/{customerName}")
    fun getOrderBooksByCustomerName(@PathVariable customerName: String): List<OrderBookResponse> {
        return OrderBookService.getOrderBooksByCustomerName(customerName)
    }
}
