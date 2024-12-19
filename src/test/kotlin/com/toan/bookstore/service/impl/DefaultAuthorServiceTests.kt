package com.toan.bookstore.service.impl

import com.toan.bookstore.domain.AuthorPatchRequestDto
import com.toan.bookstore.repository.AuthorRepository
import com.toan.bookstore.service.AuthorService
import com.toan.bookstore.testAuthorEntityA
import com.toan.bookstore.toAuthorPatchRequest
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
@Transactional
class DefaultAuthorServiceTests @Autowired constructor(
    val underTest: AuthorService,
    val authorRepository: AuthorRepository,
) {

    @Test
    fun `createAuthor persists author to database`() {
        val testAuthor = testAuthorEntityA()
        val savedAuthor = underTest.createAuthor(testAuthor)

        authorRepository.findByIdOrNull(savedAuthor.id)!!.run {
            assertThat(this).isNotNull()
            assertThat(this.id).isEqualTo(testAuthor.id)
            assertThat(this.name).isEqualTo(testAuthor.name)
            assertThat(this.age).isEqualTo(testAuthor.age)
            assertThat(this.description).isEqualTo(testAuthor.description)
        }
    }

    @Test
    fun `createAuthor throws IllegalArgumentException if authorEntity has an id`() {
        val testAuthor = testAuthorEntityA(5)
        assertThrows(IllegalArgumentException::class.java) {
            underTest.createAuthor(testAuthor)
        }
    }

    @Test
    fun `getManyAuthors returns a list of saved authors`() {
        val savedAuthor = authorRepository.save(testAuthorEntityA())
        val expected = listOf(savedAuthor)

        underTest.getManyAuthors().run {
            assertThat(size).isEqualTo(expected.size)
            assertThat(this).isEqualTo(expected)
        }
    }

    @Test
    fun `getManyAuthors returns empty list when no authors in db`() {
        underTest.getManyAuthors().run {
            assertThat(this).isEmpty()
        }
    }

    @Test
    fun `findAuthorById returns a single author when author exists`() {
        val savedAuthor = authorRepository.save(testAuthorEntityA())

        val recalledAuthor = underTest.findAuthorById(savedAuthor.id!!)
        assertThat(recalledAuthor).isEqualTo(savedAuthor)
    }

    @Test
    fun `updateAuthor throws IllegalStateException if authorEntity does not exist`() {
        val savedAuthor = authorRepository.save(testAuthorEntityA())
        assertThrows(IllegalStateException::class.java) {
            underTest.updateAuthor(savedAuthor.id!! + 666L, savedAuthor)
        }
    }

    @Test
    fun `updateAuthor updates author in db`(){
        val savedAuthor = authorRepository.save(testAuthorEntityA())
        val updateAuthor = savedAuthor.copy(age=12)
        underTest.updateAuthor(savedAuthor.id!!, updateAuthor).run {
            assertThat(id).isEqualTo(savedAuthor.id)
            assertThat(name).isEqualTo(updateAuthor.name)
            assertThat(age).isEqualTo(12)
        }
    }

    @Test
    fun `patchAuthor throws IllegalStateException if authorEntity does not exist`() {
        val savedAuthor = AuthorPatchRequestDto(
            name = "PATCHED NAME"
        )
        assertThrows(IllegalStateException::class.java) {
            underTest.patchAuthor(9999, savedAuthor.toAuthorPatchRequest())
        }
    }

    @Test
    fun `patchAuthor returns patched author on success`() {
        val author = authorRepository.save(testAuthorEntityA())
        val patchRequest = AuthorPatchRequestDto(
            name = "PATCHED NAME",
            description = "PATCHED DESCRIPTION",
            age = 99
        )

        underTest.patchAuthor(author.id!!, patchRequest.toAuthorPatchRequest())
    }

    @Test
    fun `deleteAuthor throws illegalStateException when author does not exist`() {
        assertThrows(IllegalStateException::class.java) {
            underTest.deleteAuthor(5)
        }
    }

    @Test
    fun `deleteAuthor deletes author from the db when they exist`() {
        val author = authorRepository.save(testAuthorEntityA())
        underTest.deleteAuthor(author.id!!)

        val recalledAuthor = authorRepository.findByIdOrNull(author.id!!)
        assertThat(recalledAuthor).isNull()
    }

    @Test
    fun `deleteAuthor throws illegalStateException if author does not exist`() {
        assertThrows(IllegalStateException::class.java) {
            underTest.deleteAuthor(55555)
        }
    }
}