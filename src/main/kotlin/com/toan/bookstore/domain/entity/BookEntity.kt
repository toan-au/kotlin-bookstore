package com.toan.bookstore.domain.entity

import jakarta.persistence.*

@Entity
@Table(name = "books")
data class BookEntity (
    @Id
    val isbn: String?,

    val title: String,

    val description: String,

    val image: String,

    @ManyToOne(cascade = [CascadeType.DETACH])
    @JoinColumn(name = "author_id")
    val authorEntity: AuthorEntity,
)