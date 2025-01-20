package com.toan.bookstore.service

import com.toan.bookstore.domain.BookCreateRequest
import com.toan.bookstore.domain.dto.BookDto
import com.toan.bookstore.domain.entity.BookEntity
import org.springframework.http.ResponseEntity

interface BookService {
    fun createUpdateBook(isbn: String, request: BookCreateRequest): Pair<BookEntity, Boolean>
    fun getManyBooks(authorId: Long? = null): List<BookEntity>
}