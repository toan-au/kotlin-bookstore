package com.toan.bookstore

import com.toan.bookstore.domain.dto.*
import com.toan.bookstore.domain.entity.AuthorEntity
import com.toan.bookstore.domain.entity.BookEntity

fun testAuthorDtoA(id: Long?=null) = AuthorDto(
    id = id,
    name = "Test Author A",
    age = 60,
    description = "Test description A",
    image = "image-a.png"
)

fun testAuthorDtoB(id: Long?=null) = AuthorDto(
    id = id,
    name = "Test Author B",
    age = 22,
    description = "Test description B",
    image = "image-b.png"
)

fun testAuthorDtoC(id: Long?=null) = AuthorDto(
    id = id,
    name = "Test Author C",
    age = 30,
    description = "Test description C",
    image = "image-c.png"
)

fun testAuthorEntityA(id: Long?=null) = AuthorEntity(
    id = id,
    name = "Test Author A",
    age = 60,
    description = "Test description A",
    image = "image-a.png"
)

fun testAuthorEntityB(id: Long?=null) = AuthorEntity(
    id = id,
    name = "Test Author B",
    age = 22,
    description = "Test description B",
    image = "image-b.png"
)

fun testAuthorEntityC(id: Long?=null) = AuthorEntity(
    id = id,
    name = "Test Author C",
    age = 30,
    description = "Test description C",
    image = "image-c.png"
)

fun testAuthorPatchRequestDtoA() = AuthorPatchRequestDto(
    name = "PATCHED NAME",
    description = "PATCHED DESCRIPTION",
)

fun testBookEntityA(isbn: String, author: AuthorEntity) = BookEntity(
    isbn = isbn,
    title = "Test Book A",
    description = "Description A",
    image = "book-image-a.png",
    authorEntity = author,
)

fun testBookCreateRequestDtoA(isbn: String, author: AuthorSummaryDto) = BookCreateRequestDto(
    isbn = isbn,
    title = "Test Book A",
    description = "Description A",
    image = "book-image-a.png",
    author = author,
)