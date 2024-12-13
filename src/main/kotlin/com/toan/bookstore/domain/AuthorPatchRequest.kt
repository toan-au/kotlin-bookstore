package com.toan.bookstore.domain

data class AuthorPatchRequest (
    val id: Long?,

    val name: String?,

    val age: Short?,

    val description: String?,

    val image: String?,
)