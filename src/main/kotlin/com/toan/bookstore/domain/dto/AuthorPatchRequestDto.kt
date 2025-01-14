package com.toan.bookstore.domain.dto

data class AuthorPatchRequestDto (
    val id: Long? = null,

    val name: String? = null,

    val age: Short? = null,

    val description: String? = null,

    val image: String? = null,
)