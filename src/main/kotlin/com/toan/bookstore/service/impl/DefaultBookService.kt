package com.toan.bookstore.service.impl

import com.toan.bookstore.domain.BookCreateRequest
import com.toan.bookstore.domain.BookPatchRequest
import com.toan.bookstore.domain.entity.BookEntity
import com.toan.bookstore.repository.BookRepository
import com.toan.bookstore.service.AuthorService
import com.toan.bookstore.service.BookService
import com.toan.bookstore.toEntity
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class DefaultBookService(
    private val bookRepository: BookRepository,
    private val authorService: AuthorService,
) : BookService {

    @Transactional
    override fun createUpdateBook(isbn: String, request: BookCreateRequest): Pair<BookEntity, Boolean> {
        val bookExists = bookRepository.existsById(isbn)
        val author = authorService.findAuthorById(request.authorId)
        checkNotNull(author)

        val normalisedBook = request.copy(isbn = isbn)
        val newBook = bookRepository.save(normalisedBook.toEntity(author))
        return Pair(newBook, !bookExists)
    }

    override fun getManyBooks(authorId: Long?): List<BookEntity> {
        return authorId?.let {
            bookRepository.findByAuthorEntityId(it)
        } ?: bookRepository.findAll()
    }

    override fun getBookByIsbn(isbn: String): BookEntity? =
        bookRepository.findByIdOrNull(isbn)

    override fun patchBook(isbn: String, patchRequest: BookPatchRequest): BookEntity {
        val book = bookRepository.findByIdOrNull(isbn)
        checkNotNull(book)

        return bookRepository.save(
            book.copy(
                title = patchRequest.title ?: book.title,
                description = patchRequest.description ?: book.description,
                image = patchRequest.image ?: book.image,
            )
        )
    }


}