package com.toan.bookstore.service

import com.toan.bookstore.domain.BookCreateRequest
import com.toan.bookstore.domain.entity.BookEntity

interface BookService {
    fun createUpdateBook(isbn: String, request: BookCreateRequest): Pair<BookEntity, Boolean>
}