package com.toan.bookstore

import com.toan.bookstore.domain.dto.AuthorDto
import com.toan.bookstore.domain.entity.AuthorEntity

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