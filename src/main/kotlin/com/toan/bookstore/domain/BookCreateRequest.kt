package com.toan.bookstore.domain

data class BookCreateRequest(
    val isbn: String,

    val title: String,

    val description: String,

    val image: String,

    val authorId: Long,
)