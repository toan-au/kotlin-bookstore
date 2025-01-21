package com.toan.bookstore

import com.toan.bookstore.domain.AuthorPatchRequest
import com.toan.bookstore.domain.BookCreateRequest
import com.toan.bookstore.domain.BookPatchRequest
import com.toan.bookstore.domain.dto.*
import com.toan.bookstore.domain.entity.AuthorEntity
import com.toan.bookstore.domain.entity.BookEntity

fun AuthorEntity.toDto(): AuthorDto = AuthorDto(
    id = this.id,
    name = this.name,
    age = this.age,
    description = this.description,
    image = this.image,
)

fun AuthorDto.toEntity(): AuthorEntity = AuthorEntity(
    id = this.id,
    name = this.name,
    age = this.age,
    description = this.description,
    image = this.image,
)

fun BookEntity.toDto(): BookDto = BookDto(
    isbn = this.isbn,
    title = this.title,
    description = this.description,
    image = this.image,
    author = this.authorEntity.toDto(),
)

fun BookDto.toEntity(): BookEntity = BookEntity(
    isbn = this.isbn,
    title = this.title,
    description = this.description,
    image = this.image,
    authorEntity = this.author.toEntity(),
)

fun BookCreateRequestDto.toBookCreateRequest(): BookCreateRequest = BookCreateRequest(
    isbn = isbn,
    title = this.title,
    description = this.description,
    image = this.image,
    authorId = author.id,
)

fun BookCreateRequest.toEntity(author: AuthorEntity): BookEntity = BookEntity(
    isbn = isbn,
    title = this.title,
    description = this.description,
    image = this.image,
    authorEntity = author,
)

fun AuthorPatchRequestDto.toAuthorPatchRequest(): AuthorPatchRequest = AuthorPatchRequest(
    id = this.id,
    name = this.name,
    age = this.age,
    description = this.description,
    image = this.image,
)

fun BookPatchRequestDto.toBookPatchRequest(): BookPatchRequest = BookPatchRequest(
    title = this.title,
    description = this.description,
    image = this.image,
)