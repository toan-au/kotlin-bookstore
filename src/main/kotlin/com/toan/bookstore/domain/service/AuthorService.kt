package com.toan.bookstore.domain.service

import com.toan.bookstore.domain.entity.AuthorEntity

interface AuthorService {

    fun saveAuthor(author: AuthorEntity): AuthorEntity
    fun findAuthorById(id: Long): AuthorEntity?
    fun getManyAuthors(): List<AuthorEntity>
}