package com.toan.bookstore.domain.dto

data class BookPatchRequestDto (
    val title: String? = null,

    val description: String? = null,

    val image: String? = null,
)