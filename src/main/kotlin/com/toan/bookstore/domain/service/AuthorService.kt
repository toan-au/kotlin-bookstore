package com.toan.bookstore.domain.service

import com.toan.bookstore.domain.AuthorPatchRequest
import com.toan.bookstore.domain.entity.AuthorEntity

interface AuthorService {

    fun createAuthor(author: AuthorEntity): AuthorEntity
    fun updateAuthor(id: Long, author: AuthorEntity): AuthorEntity
    fun patchAuthor(id: Long, updateRequest: AuthorPatchRequest): AuthorEntity
    fun findAuthorById(id: Long): AuthorEntity?
    fun getManyAuthors(): List<AuthorEntity>
}