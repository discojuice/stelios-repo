package com.example.demo.service;

import com.example.demo.dto.BookCreateUpdateRequest;
import com.example.demo.dto.BookDto;
import com.example.demo.entity.Author;
import com.example.demo.entity.Book;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepo;
    private final AuthorRepository authorRepo;

    public BookServiceImpl(BookRepository bookRepo, AuthorRepository authorRepo) {
        this.bookRepo = bookRepo;
        this.authorRepo = authorRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> getAll() {
        return bookRepo.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public BookDto create(BookCreateUpdateRequest req) {
        validateRequest(req);

        Author author = authorRepo.findById(req.authorId)
                .orElseThrow(() -> new NotFoundException("Author not found: " + req.authorId));

        Book b = new Book();
        b.setTitle(req.title.trim());
        b.setAuthor(author);

        return toDto(bookRepo.save(b));
    }

    @Override
    public BookDto update(Long id, BookCreateUpdateRequest req) {
        if (id == null) {
            throw new BadRequestException("Book id is required");
        }
        validateRequest(req);

        Book existing = bookRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found: " + id));

        Author author = authorRepo.findById(req.authorId)
                .orElseThrow(() -> new NotFoundException("Author not found: " + req.authorId));

        existing.setTitle(req.title.trim());
        existing.setAuthor(author);

        return toDto(bookRepo.save(existing));
    }

    @Override
    public boolean delete(Long id) {
        if (id == null) {
            throw new BadRequestException("Book id is required");
        }

        Book existing = bookRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found: " + id));

        bookRepo.delete(existing);
        return true;
    }

    private void validateRequest(BookCreateUpdateRequest req) {
        if (req == null) {
            throw new BadRequestException("Request body is required");
        }
        if (req.title == null || req.title.trim().isEmpty()) {
            throw new BadRequestException("Title is required");
        }
        if (req.authorId == null) {
            throw new BadRequestException("authorId is required");
        }
    }

    private BookDto toDto(Book b) {
        BookDto dto = new BookDto();
        dto.id = b.getId();
        dto.title = b.getTitle();
        dto.authorId = b.getAuthor().getId();
        dto.authorName = b.getAuthor().getName();
        return dto;
    }
}
