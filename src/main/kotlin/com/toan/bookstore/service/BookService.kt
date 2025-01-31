package com.toan.bookstore.service

import com.toan.bookstore.domain.BookCreateRequest
import com.toan.bookstore.domain.BookPatchRequest
import com.toan.bookstore.domain.entity.BookEntity

interface BookService {
    fun createUpdateBook(isbn: String, request: BookCreateRequest): Pair<BookEntity, Boolean>
    fun getManyBooks(authorId: Long? = null): List<BookEntity>
    fun getBookByIsbn(isbn: String): BookEntity?
    fun patchBook(isbn: String, patchRequest: BookPatchRequest): BookEntity
    fun deleteBook(isbn: String)
}