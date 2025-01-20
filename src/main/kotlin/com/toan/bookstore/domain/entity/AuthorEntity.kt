package com.toan.bookstore.domain.entity

import jakarta.persistence.*

@Entity
@Table(name = "authors")
data class AuthorEntity (

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_id_seq")
    val id: Long?,

    val name: String,

    val age: Short,

    val description: String,

    val image: String,

    @OneToMany(cascade = [CascadeType.REMOVE], orphanRemoval = true, mappedBy = "authorEntity")
    val books: MutableList<BookEntity>? = mutableListOf(),
)