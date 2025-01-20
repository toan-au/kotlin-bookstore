package com.toan.bookstore.repository

import com.toan.bookstore.domain.entity.BookEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository : JpaRepository<BookEntity, String?> {
    fun findByAuthorEntityId(authorId: Long): List<BookEntity>
}