package com.toan.bookstore.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.toan.bookstore.*
import com.toan.bookstore.domain.service.AuthorService
import io.mockk.every
import io.mockk.verify
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
class AuthorControllerTests @Autowired constructor(
    val mockMvc: MockMvc,
    @MockkBean val authorService: AuthorService,
    val mapper: ObjectMapper
){

    @BeforeEach
    fun beforeEach() {
        every {
            authorService.saveAuthor(any())
        } answers {
            firstArg()
        }
    }

    @Test
    fun `saveAuthor returns HTTP 201 CREATED on success`() {
        val testAuthor = testAuthorDtoA()
        mockMvc.post("/v1/authors") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(testAuthor)
        }.andExpect {
            status { isCreated() }
        }
    }

    @Test
    fun `saveAuthor calls authorService to save author on success`() {
        val testAuthor = testAuthorDtoA()
        mockMvc.post("/v1/authors") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(testAuthor)
        }

        verify {
            authorService.saveAuthor(testAuthor.toEntity())
        }
    }

    @Test
    fun `getManyAuthors returns HTTP 200 and empty list when there are no authors`() {
        every {
            authorService.getManyAuthors()
        } answers {
            emptyList()
        }

        mockMvc.get("/v1/authors") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { json("[]") }
        }
    }

    @Test
    fun `getManyAuthors returns HTTP 200 and complete list when there are authors`() {
        every {
            authorService.getManyAuthors()
        } answers {
            listOf(
                testAuthorEntityA(1),
                testAuthorEntityB(2),
                testAuthorEntityC(3)
            )
        }

        mockMvc.get("/v1/authors") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { jsonPath("$[0].id", equalTo(1)) }
            content { jsonPath("$[1].id", equalTo(2)) }
            content { jsonPath("$[2].id", equalTo(3)) }
        }
    }

    @Test
    fun `findAuthorById returns 404 when author does not exist`() {
        every {
            authorService.findAuthorById(any())
        } answers {
            null
        }

        mockMvc.get("/v1/authors/90000") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isNotFound() }
        }
    }

    @Test
    fun `findAuthorById returns 200 and single author when author is exists`() {
        val testAuthor = authorService.saveAuthor(testAuthorEntityA(2))

        every {
            authorService.findAuthorById(any())
        } answers {
            testAuthor
        }

        mockMvc.get("/v1/authors/${testAuthor.id}") {
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { jsonPath("$.id", equalTo(testAuthor.id?.toInt())) }
            content { jsonPath("$.name", equalTo(testAuthor.name)) }
            content { jsonPath("$.age", equalTo(testAuthor.age.toInt())) }
            content { jsonPath("$.description", equalTo(testAuthor.description)) }
            content { jsonPath("$.image", equalTo(testAuthor.image)) }
        }
    }
}