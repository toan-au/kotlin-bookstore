package com.toan.bookstore.controller

import com.toan.bookstore.domain.dto.AuthorDto
import com.toan.bookstore.domain.dto.AuthorPatchRequestDto
import com.toan.bookstore.service.AuthorService
import com.toan.bookstore.toAuthorPatchRequest
import com.toan.bookstore.toDto
import com.toan.bookstore.toEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/authors")
class AuthorsController(
    private val authorService: AuthorService
){

    @PostMapping
    fun createAuthor(@RequestBody authorDto: AuthorDto): ResponseEntity<AuthorDto> {
        return try {
            val savedAuthor = authorService.createAuthor(authorDto.toEntity())
            ResponseEntity(savedAuthor.toDto(), HttpStatus.CREATED)
        } catch (ex: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }

    }

    @GetMapping("/{id}")
    fun getAuthor(@PathVariable id: Long): ResponseEntity<AuthorDto> {
        authorService.findAuthorById(id)?.let {
            return ResponseEntity(it.toDto(), HttpStatus.OK)
        }
        return ResponseEntity.notFound().build()
    }

    @GetMapping
    fun getManyAuthors(): ResponseEntity<List<AuthorDto>> {
        val authors = authorService.getManyAuthors().map {
            it.toDto()
        }.toList()
        return ResponseEntity.ok(authors)
    }

    @PutMapping("/{id}")
    fun fullUpdateAuthor(
        @PathVariable id: Long,
        @RequestBody authorDto: AuthorDto
    ): ResponseEntity<AuthorDto> {
        return try {
            val updatedAuthor = authorService.updateAuthor(id, authorDto.toEntity())
            ResponseEntity.ok(updatedAuthor.toDto())
        } catch (ex: IllegalStateException) {
            ResponseEntity.notFound().build()
        }
    }

    @PatchMapping("/{id}")
    fun partialUpdateAuthor(
        @PathVariable id: Long,
        @RequestBody patchRequest: AuthorPatchRequestDto
    ): ResponseEntity<AuthorDto> {
        return try {
            val patchedAuthor = authorService.patchAuthor(id, patchRequest.toAuthorPatchRequest())
            ResponseEntity.ok(patchedAuthor.toDto())
        } catch (ex: IllegalStateException) {
             ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteAuthor(
        @PathVariable id: Long,
    ): ResponseEntity<Unit> {
        return try {
            authorService.deleteAuthor(id)
            ResponseEntity.noContent().build()
        } catch (ex: IllegalStateException) {
            ResponseEntity.notFound().build()
        }

    }
}