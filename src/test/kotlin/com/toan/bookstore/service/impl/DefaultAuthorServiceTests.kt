package com.toan.bookstore.service.impl

import com.toan.bookstore.domain.entity.AuthorEntity
import com.toan.bookstore.domain.service.AuthorService
import com.toan.bookstore.repository.AuthorRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
class DefaultAuthorServiceTests @Autowired constructor(
    val underTest: AuthorService,
    val authorRepository: AuthorRepository,
) {

    @Test
    fun `createAuthor persists author to database`() {
        val testAuthor = AuthorEntity(
            id = null,
            name = "Test Author",
            age = 54,
            description = "The greatest Author in Latin America",
            image = "authorA.png"
        )
        val savedAuthor = underTest.saveAuthor(testAuthor)

        authorRepository.findByIdOrNull(savedAuthor.id).run {
            assertThat(this).isNotNull()
            assertThat(this?.id).isEqualTo(testAuthor.id)
            assertThat(this?.name).isEqualTo(testAuthor.name)
            assertThat(this?.age).isEqualTo(testAuthor.age)
            assertThat(this?.description).isEqualTo(testAuthor.description)
        }
    }
}