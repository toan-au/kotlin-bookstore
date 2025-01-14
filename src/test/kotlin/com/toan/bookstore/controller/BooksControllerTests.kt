package com.toan.bookstore.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.toan.bookstore.domain.dto.AuthorSummaryDto
import com.toan.bookstore.service.BookService
import com.toan.bookstore.testAuthorEntityA
import com.toan.bookstore.testBookCreateRequestDtoA
import com.toan.bookstore.testBookEntityA
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.put
import org.springframework.test.web.servlet.result.StatusResultMatchersDsl

@SpringBootTest
@AutoConfigureMockMvc
class BooksControllerTests @Autowired constructor(
    val mockMvc: MockMvc,
    @MockkBean val bookService: BookService,
    val mapper: ObjectMapper
){

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
        val book = testBookEntityA(isbn, author)
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
}