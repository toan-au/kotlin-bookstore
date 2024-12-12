package com.toan.bookstore.service.impl

import com.toan.bookstore.domain.service.AuthorService
import com.toan.bookstore.repository.AuthorRepository
import com.toan.bookstore.testAuthorEntityA
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class DefaultAuthorServiceTests @Autowired constructor(
    val underTest: AuthorService,
    val authorRepository: AuthorRepository,
) {

    @Test
    fun `createAuthor persists author to database`() {
        val testAuthor = testAuthorEntityA()
        val savedAuthor = underTest.saveAuthor(testAuthor)

        authorRepository.findByIdOrNull(savedAuthor.id)!!.run {
            assertThat(this).isNotNull()
            assertThat(this.id).isEqualTo(testAuthor.id)
            assertThat(this.name).isEqualTo(testAuthor.name)
            assertThat(this.age).isEqualTo(testAuthor.age)
            assertThat(this.description).isEqualTo(testAuthor.description)
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
}