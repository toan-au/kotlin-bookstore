package com.toan.bookstore.controller

import com.toan.bookstore.domain.dto.BookCreateRequestDto
import com.toan.bookstore.domain.dto.BookDto
import com.toan.bookstore.service.BookService
import com.toan.bookstore.toBookCreateRequest
import com.toan.bookstore.toDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/books")
class BooksController(val bookService: BookService) {

    @PutMapping("/{isbn}")
    fun createBook(
        @PathVariable isbn: String,
        @RequestBody request: BookCreateRequestDto
    ): ResponseEntity<BookDto> {
        return try {
            val (newBook, created) = bookService.createUpdateBook(isbn, request.toBookCreateRequest())
            val statusCode = if (created) HttpStatus.CREATED else HttpStatus.OK

            return ResponseEntity(newBook.toDto(), statusCode)
        } catch (e: IllegalStateException) {
             ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    @GetMapping
    fun getBooks(@RequestParam author: Long?): ResponseEntity<List<BookDto>> {
        val recalledBooks =  bookService.getManyBooks(author).map {
            it.toDto()
        }
        return ResponseEntity(recalledBooks, HttpStatus.OK)
    }
}