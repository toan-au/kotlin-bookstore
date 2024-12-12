package com.toan.bookstore.domain.service.impl

import com.toan.bookstore.domain.entity.AuthorEntity
import com.toan.bookstore.domain.service.AuthorService
import com.toan.bookstore.repository.AuthorRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service


@Service
class DefaultAuthorService (
    val authorRepository: AuthorRepository
) : AuthorService {
    override fun saveAuthor(author: AuthorEntity): AuthorEntity =
        authorRepository.save(author)


    override fun findAuthorById(id: Long): AuthorEntity? =
        authorRepository.findByIdOrNull(id)

    override fun getManyAuthors(): List<AuthorEntity> =
        authorRepository.findAll()

}