package com.toan.bookstore.service.impl

import com.toan.bookstore.BOOK_A_ISBN
import com.toan.bookstore.domain.BookCreateRequest
import com.toan.bookstore.repository.AuthorRepository
import com.toan.bookstore.repository.BookRepository
import com.toan.bookstore.service.BookService
import com.toan.bookstore.testAuthorEntityA
import com.toan.bookstore.testBookCreateRequestA
import com.toan.bookstore.testBookEntityA
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@Transactional
@SpringBootTest
class DefaultBookServiceTests @Autowired constructor(
    private val bookRepository: BookRepository,
    private val underTest: BookService
) {

    @Autowired
    private lateinit var authorRepository: AuthorRepository

    @Test
    fun `createUpdateBook throws illegalStateException if author not found`() {

        val createRequest = testBookCreateRequestA(BOOK_A_ISBN, 999L)


        assertThrows<IllegalStateException> {
            underTest.createUpdateBook(BOOK_A_ISBN, createRequest)
        }
    }

    @Test
    fun `createUpdateBook creates a new Book when ISBN does not exist`() {
        val author = authorRepository.save(testAuthorEntityA())
        val createRequest = testBookCreateRequestA(BOOK_A_ISBN, author.id!!)
        val isbn = "this-isbn-does-not-exist"


        val (bookResponse, isCreated) = underTest.createUpdateBook(isbn, createRequest)
        val recalledBook = bookRepository.findByIdOrNull(isbn)

        assertThat(isCreated).isTrue()

        assertThat(recalledBook).isNotNull()
        assertThat(recalledBook).isEqualTo(bookResponse)
    }

    @Test
    fun `createUpdateBook updates an existing book when the ISBN exists in the db`() {
        val author = authorRepository.save(testAuthorEntityA())
        val book = testBookEntityA(BOOK_A_ISBN, author)

        bookRepository.save(book)

        val updateRequest = BookCreateRequest(
            isbn = BOOK_A_ISBN,
            title = "UPDATED TITLE",
            description = "UPDATED DESCRIPTION",
            image = book.image,
            authorId = author.id!!
        )

        val (updatedBook, isCreated) = underTest.createUpdateBook(BOOK_A_ISBN, updateRequest)
        val recalledBook = bookRepository.findByIdOrNull(BOOK_A_ISBN)

        assertThat(recalledBook).isNotNull()

        assertThat(isCreated).isFalse()
        assertThat(recalledBook).isEqualTo(updatedBook)
    }

    @Test
    fun `getManyBooks returns a list of books`() {
        
    }

    @Test
    fun `getManyBooks returns an empty list when there are no applicable books`() {

    }

    @Test
    fun `getManyBooks returns empty list when authorId does not match any books`() {
        val savedAuthor = authorRepository.save(testAuthorEntityA())
        assertThat(savedAuthor).isNotNull()

        val savedBook = bookRepository.save(testBookEntityA(BOOK_A_ISBN, savedAuthor))
        assertThat(savedBook).isNotNull()

        val recalledBooks = underTest.getManyBooks(authorId = 999L)
        assertThat(recalledBooks).isEmpty()
    }

    @Test
    fun `getManyBooks returns list of books when authorId does match`() {
        val savedAuthor = authorRepository.save(testAuthorEntityA())
        assertThat(savedAuthor).isNotNull()

        val savedBook = bookRepository.save(testBookEntityA(BOOK_A_ISBN, savedAuthor))
        assertThat(savedBook).isNotNull()

        val recalledBooks = underTest.getManyBooks(authorId = savedAuthor.id!!)
        assertThat(recalledBooks).hasSize(1)
        assertThat(recalledBooks[0]).isEqualTo(savedBook)
    }

    @Test
    fun `getBookByIsbn returns a BookEntity when the ISBN exists`() {
        val testAuthor = authorRepository.save(testAuthorEntityA())
        val savedBook = bookRepository.save(testBookEntityA(BOOK_A_ISBN, testAuthor))

        val recalledBook = underTest.getBookByIsbn(BOOK_A_ISBN)
        assertThat(recalledBook).isNotNull()
        assertThat(recalledBook).isEqualTo(savedBook)
    }

    @Test
    fun `getBookByIsbn returns null when the ISBN does not exist`() {
        val recalledBook = underTest.getBookByIsbn("this-isbn-does-not-exist")
        assertThat(recalledBook).isNull()
    }
}