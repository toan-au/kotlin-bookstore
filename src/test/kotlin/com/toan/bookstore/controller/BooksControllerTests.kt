package com.toan.bookstore.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.toan.bookstore.*
import com.toan.bookstore.domain.dto.AuthorSummaryDto
import com.toan.bookstore.repository.AuthorRepository
import com.toan.bookstore.repository.BookRepository
import com.toan.bookstore.service.BookService
import io.mockk.every
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*
import org.springframework.test.web.servlet.result.StatusResultMatchersDsl

@SpringBootTest
@AutoConfigureMockMvc
class BooksControllerTests @Autowired constructor(
    val mockMvc: MockMvc,
    @MockkBean val bookService: BookService,
    val mapper: ObjectMapper
){

    @Autowired
    private lateinit var authorRepository: AuthorRepository

    @Autowired
    private lateinit var bookRepository: BookRepository

    @Test
    fun `createBook returns 201 CREATED on success`(){
        assertThatUserCreatedUpdate(true) { isCreated() }
    }

    @Test
    fun `createBook returns 200 OK when book is updated`(){
        assertThatUserCreatedUpdate(false) { isOk() }
    }

    private fun assertThatUserCreatedUpdate(created: Boolean, statusCodeAssertion: StatusResultMatchersDsl.() -> Unit) {
        val isbn = "123-456-789"
        val author = testAuthorEntityA(1)
        val book = testBookEntityA(isbn, author)
        val createRequest = testBookCreateRequestDtoA(isbn, AuthorSummaryDto(author.id!!))

        every {
            bookService.createUpdateBook(any(), any())
        } answers {
            Pair(book, created)
        }

        mockMvc.put("/v1/books/${isbn}") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(createRequest)
        }.andExpect {
            status { statusCodeAssertion() }
        }
    }

    @Test
    fun `createBook returns 400 BAD_REQUEST when authorId does not exist`() {
        val isbn = "123-456-789"
        val author = testAuthorEntityA(1)
        val createRequest = testBookCreateRequestDtoA(isbn, AuthorSummaryDto(author.id!!))

        every {
            bookService.createUpdateBook(any(), any())
        } throws IllegalStateException()

        mockMvc.put("/v1/books/${isbn}") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(createRequest)
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `getManyBooks returns a list of books and status code 200`() {
        val authorA = authorRepository.save(testAuthorEntityA())
        val authorB = authorRepository.save(testAuthorEntityB())
        val bookA = bookRepository.save(testBookEntityA(BOOK_A_ISBN, authorA))
        val bookB = bookRepository.save(testBookEntityB(BOOK_B_ISBN, authorB))

        every {
            bookService.getManyBooks()
        } answers {
            listOf(bookA, bookB)
        }

        mockMvc.get("/v1/books") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { jsonPath("$[0].isbn", equalTo(BOOK_A_ISBN)) }
            content { jsonPath("$[0].title", equalTo(bookA.title)) }

            content { jsonPath("$[1].isbn", equalTo(BOOK_B_ISBN)) }
            content { jsonPath("$[1].title", equalTo(bookB.title)) }
        }
    }

    @Test
    fun `getManyBooks returns an empty list and status code 200 when there are no applicable books`() {
        every {
            bookService.getManyBooks()
        } answers {
            emptyList()
        }

        mockMvc.get("/v1/books") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { string("[]") }
        }
    }

    @Test
    fun `getManyBooks returns an empty list when authorId has no books`() {
        every {
            bookService.getManyBooks(authorId = any())
        } answers {
            emptyList()
        }

        mockMvc.get("/v1/books?author=99999") {
            contentType = MediaType.APPLICATION_JSON
            accept(MediaType.APPLICATION_JSON)
        }.andExpect {
            status { isOk() }
            content { json("[]") }
        }
    }

    @Test
    fun `getManyBooks returns list of books that match the authorId`() {
        every {
            bookService.getManyBooks(authorId = 1)
        } answers {
            listOf(
                testBookEntityA(BOOK_A_ISBN, testAuthorEntityA(1L))
            )
        }

        mockMvc.get("/v1/books?author=1") {
            contentType = MediaType.APPLICATION_JSON
            accept(MediaType.APPLICATION_JSON)
        }.andExpect {
            status { isOk() }
            content { jsonPath("$[0].isbn", equalTo(BOOK_A_ISBN)) }
            content { jsonPath("$[0].author.id", equalTo(1)) }
        }
    }

    @Test
    fun `getBook returns 404 NOT_FOUND when book with given ISBN does not exist`() {
        every {
            bookService.getBookByIsbn(any())
        } answers {
            null
        }

        mockMvc.get("/v1/books/$BOOK_A_ISBN") {
            contentType = MediaType.APPLICATION_JSON
            accept(MediaType.APPLICATION_JSON)
        }.andExpect {
            status { isNotFound() }
        }
    }

    @Test
    fun `getBook returns 200 OK when book with given ISBN exists`() {

        val book = testBookEntityA(BOOK_A_ISBN, testAuthorEntityA(1L))
        every {
            bookService.getBookByIsbn(any())
        } answers {
            book
        }

        mockMvc.get("/v1/books/$BOOK_A_ISBN") {
            contentType = MediaType.APPLICATION_JSON
            accept(MediaType.APPLICATION_JSON)
        }.andExpect {
            status { isOk() }
            content { jsonPath("$.isbn", equalTo(BOOK_A_ISBN)) }
            content { jsonPath("$.title", equalTo(book.title)) }
            content { jsonPath("$.description", equalTo(book.description)) }
            content { jsonPath("$.image", equalTo(book.image)) }
        }
    }

    @Test
    fun `patchBook returns NOT_FOUND when isbn does not exist`() {
        every {
            bookService.patchBook(any(), any())
        } throws IllegalStateException("Book not found")

        val patchRequest = testBookPatchRequestDtoA()

        mockMvc.patch("/v1/books/$BOOK_A_ISBN") {
            contentType = MediaType.APPLICATION_JSON
            accept(MediaType.APPLICATION_JSON)
            content = mapper.writeValueAsString(patchRequest)
        }.andExpect {
            status { isNotFound() }
        }
    }

    @Test
    fun `patchBook returns an updated book and STATUS OK on success`() {
        val book = testBookEntityA(BOOK_A_ISBN, testAuthorEntityA(1L))
        val patchRequest = testBookPatchRequestDtoA()

        every {
            bookService.patchBook(any(), any())
        } answers {
            book.copy(
                title = patchRequest.title!!,
                description = patchRequest.description!!,
            )
        }

        mockMvc.patch("/v1/books/$BOOK_A_ISBN") {
            contentType = MediaType.APPLICATION_JSON
            accept(MediaType.APPLICATION_JSON)
            content = mapper.writeValueAsString(patchRequest)
        }.andExpect {
            status { isOk() }
            content { jsonPath("$.isbn", equalTo(book.isbn)) }
            content { jsonPath("$.title", equalTo(patchRequest.title)) }
            content { jsonPath("$.description", equalTo(patchRequest.description)) }
        }
    }

    @Test
    fun `deleteBook returns 204 NO_CONTENT when any isbn is provided`() {
        every {
            bookService.deleteBook(any())
        } answers { }

        mockMvc.delete("/v1/books/$BOOK_A_ISBN") {
            contentType = MediaType.APPLICATION_JSON
            accept(MediaType.APPLICATION_JSON)
        }.andExpect {
            status { isNoContent() }
        }
    }
}