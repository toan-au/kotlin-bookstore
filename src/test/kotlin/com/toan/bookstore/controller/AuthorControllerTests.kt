package com.toan.bookstore.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.toan.bookstore.*
import com.toan.bookstore.domain.AuthorPatchRequestDto
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
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.*

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AuthorControllerTests @Autowired constructor(
    val mockMvc: MockMvc,
    @MockkBean val authorService: AuthorService,
    val mapper: ObjectMapper
){

    @BeforeEach
    fun beforeEach() {
        every {
            authorService.createAuthor(any())
        } answers {
            firstArg()
        }
    }

    @Test
    fun `createAuthor returns HTTP 201 CREATED on success`() {
        val testAuthor = testAuthorDtoA()
        mockMvc.post("/v1/authors") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(testAuthor)
        }.andExpect {
            status { isCreated() }
        }
    }


    @Test
    fun `createAuthor calls authorService to save author on success`() {
        val testAuthor = testAuthorDtoA()
        mockMvc.post("/v1/authors") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(testAuthor)
        }

        verify {
            authorService.createAuthor(testAuthor.toEntity())
        }
    }

    @Test
    fun `createAuthor returns 400 when author is given an id`() {
        every {
            authorService.createAuthor(any())
        } throws IllegalArgumentException()

        val testAuthor = testAuthorDtoA(5)
        mockMvc.post("/v1/authors") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(testAuthor)
        }.andExpect {
            status { isBadRequest() }
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
        val testAuthor = authorService.createAuthor(testAuthorEntityA(2))

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

    @Test
    fun `updateAuthor returns 404 NOT FOUND when author does not exist`() {
        every {
            authorService.updateAuthor(any(), any())
        } throws IllegalStateException()

        val author = testAuthorDtoA()

        mockMvc.put("/v1/authors/999") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(author)
        }.andExpect {
            status { isNotFound() }
        }
    }

    @Test
    fun `updateAuthor returns 200 OK and updated author on success`() {
        val author = testAuthorDtoA(2)
        val update = author.copy(name = "UPDATED")

        every {
            authorService.updateAuthor(any(), any())
        } answers {
            update.toEntity()
        }

        mockMvc.put("/v1/authors/2") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(update)
        }.andExpect {
            status { isOk() }
            jsonPath("$.name", equalTo("UPDATED"))
        }
    }

    @Test
    fun `patchAuthor returns 404 NOT FOUND when author does not exist`() {
        every {
            authorService.patchAuthor(any(), any())
        } throws IllegalStateException()

        mockMvc.patch("/v1/authors/999") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(testAuthorPatchRequestDtoA())
        }.andExpect {
            status { isNotFound() }
        }
    }

    @Test
    fun `patchAuthor returns 200 OK and patched author on success`() {
        val author = testAuthorEntityA(2)
        val patchRequest = AuthorPatchRequestDto(
            name = "PATCHED NAME",
            description = "PATCHED DESCRIPTION",
        )

        every {
            authorService.patchAuthor(any(), any())
        } answers {
            author.copy(
                name = patchRequest.name!!,
                description = patchRequest.description!!,
            )
        }

        mockMvc.patch("/v1/authors/${author.id}") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(testAuthorPatchRequestDtoA())
        }.andExpect {
            status { isOk() }
            jsonPath("$.id", equalTo(author.id?.toInt()))
            jsonPath("$.name", equalTo(patchRequest.name))
            jsonPath("$.description", equalTo(patchRequest.description))
            jsonPath("$.image", equalTo(author.image))
        }
    }
}