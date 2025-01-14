package com.toan.bookstore.domain.dto

data class BookCreateRequestDto(
    val isbn: String,

    val title: String,

    val description: String,

    val image: String,

    val author: AuthorSummaryDto,
)