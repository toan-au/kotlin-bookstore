package com.toan.bookstore.domain

data class BookPatchRequest (
    val title: String? = null,

    val description: String? = null,

    val image: String? = null,
)