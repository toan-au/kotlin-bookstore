package com.toan.bookstore.domain.service.impl

import com.toan.bookstore.domain.AuthorPatchRequest
import com.toan.bookstore.domain.entity.AuthorEntity
import com.toan.bookstore.domain.service.AuthorService
import com.toan.bookstore.repository.AuthorRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service


@Service
class DefaultAuthorService (
    val authorRepository: AuthorRepository
) : AuthorService {
    override fun createAuthor(author: AuthorEntity): AuthorEntity {
        require(author.id == null)
        return authorRepository.save(author)
    }

    @Transactional
    override fun updateAuthor(id: Long, author: AuthorEntity): AuthorEntity {
        checkNotNull(authorRepository.findByIdOrNull(id))
        val normalizedAuthor = author.copy(id = id)
        return authorRepository.save(normalizedAuthor)
    }

    @Transactional
    override fun patchAuthor(id: Long, updateRequest: AuthorPatchRequest): AuthorEntity {
        val existingAuthor = authorRepository.findByIdOrNull(id)
        checkNotNull(existingAuthor)

        val updatedAuthor = existingAuthor.copy(
            name = updateRequest.name ?: existingAuthor.name,
            age = updateRequest.age ?: existingAuthor.age,
            description = updateRequest.description ?: existingAuthor.description,
            image = updateRequest.image ?: existingAuthor.image,
        )

        return authorRepository.save(updatedAuthor)
    }

    override fun findAuthorById(id: Long): AuthorEntity? =
        authorRepository.findByIdOrNull(id)

    override fun getManyAuthors(): List<AuthorEntity> =
        authorRepository.findAll()

}