package com.example.demo.service;

import com.example.demo.dto.BookCreateUpdateRequest;
import com.example.demo.dto.BookDto;
import com.example.demo.entity.Author;
import com.example.demo.entity.Book;
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
        if (req == null || req.title == null || req.title.isBlank() || req.authorId == null) return null;

        Author author = authorRepo.findById(req.authorId).orElse(null);
        if (author == null) return null;

        Book b = new Book();
        b.setTitle(req.title);
        b.setAuthor(author);

        return toDto(bookRepo.save(b));
    }

    @Override
    public BookDto update(Long id, BookCreateUpdateRequest req) {
        return bookRepo.findById(id).map(existing -> {
            if (req.title != null && !req.title.isBlank()) {
                existing.setTitle(req.title);
            }

            if (req.authorId != null) {
                Author author = authorRepo.findById(req.authorId).orElse(null);
                if (author == null) return null; // invalid authorId
                existing.setAuthor(author);
            }

            return toDto(bookRepo.save(existing));
        }).orElse(null);
    }

    @Override
    public boolean delete(Long id) {
        if (!bookRepo.existsById(id)) return false;
        bookRepo.deleteById(id);
        return true;
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
