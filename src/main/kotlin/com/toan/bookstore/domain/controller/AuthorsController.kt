package com.toan.bookstore.domain.controller

import com.toan.bookstore.domain.dto.AuthorDto
import com.toan.bookstore.domain.service.AuthorService
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
        val savedAuthor = authorService.saveAuthor(authorDto.toEntity())
        return ResponseEntity(savedAuthor.toDto(), HttpStatus.CREATED)
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
}