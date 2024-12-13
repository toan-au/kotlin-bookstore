package com.toan.bookstore.domain.dto

data class AuthorDto(
    val id: Long?,

    val name: String,

    val age: Short,

    val description: String,

    val image: String,
)