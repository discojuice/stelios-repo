package com.example.demo.controller;

import com.example.demo.dto.BookCreateUpdateRequest;
import com.example.demo.dto.BookDto;
import com.example.demo.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<BookDto> all() {
        return bookService.getAll();
    }

    @PostMapping
    public ResponseEntity<BookDto> create(@RequestBody BookCreateUpdateRequest req) {
        BookDto created = bookService.create(req);
        return created == null ? ResponseEntity.badRequest().build() : ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> update(@PathVariable Long id, @RequestBody BookCreateUpdateRequest req) {
        BookDto updated = bookService.update(id, req);
        // could be not found OR bad authorId; we keep it simple:
        return updated == null ? ResponseEntity.badRequest().build() : ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = bookService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}


//package com.example.demo.controller;
//
//import com.example.demo.dto.BookCreateUpdateRequest;
//import com.example.demo.dto.BookDto;
//import com.example.demo.entity.Author;
//import com.example.demo.entity.Book;
//import com.example.demo.repository.AuthorRepository;
//import com.example.demo.repository.BookRepository;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@CrossOrigin(origins = "http://localhost:4200")
//@RestController
//@RequestMapping("/api/books")
//public class BookController {
//
//    private final BookRepository bookRepo;
//    private final AuthorRepository authorRepo;
//
//    public BookController(BookRepository bookRepo, AuthorRepository authorRepo) {
//        this.bookRepo = bookRepo;
//        this.authorRepo = authorRepo;
//    }
//
//    @GetMapping
//    public List<BookDto> all() {
//        return bookRepo.findAll().stream().map(b -> {
//            BookDto dto = new BookDto();
//            dto.id = b.getId();
//            dto.title = b.getTitle();
//            dto.authorId = b.getAuthor().getId();
//            dto.authorName = b.getAuthor().getName();
//            return dto;
//        }).toList();
//    }
//
//    @PostMapping
//    public ResponseEntity<BookDto> create(@RequestBody BookCreateUpdateRequest req) {
//        if (req.title == null || req.title.isBlank() || req.authorId == null) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        Author author = authorRepo.findById(req.authorId).orElse(null);
//        if (author == null) return ResponseEntity.badRequest().build();
//
//        Book b = new Book();
//        b.setTitle(req.title);
//        b.setAuthor(author);
//        b = bookRepo.save(b);
//
//        BookDto dto = new BookDto();
//        dto.id = b.getId();
//        dto.title = b.getTitle();
//        dto.authorId = author.getId();
//        dto.authorName = author.getName();
//
//        return ResponseEntity.ok(dto);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<BookDto> update(@PathVariable Long id, @RequestBody BookCreateUpdateRequest req) {
//        return (ResponseEntity<BookDto>) bookRepo.findById(id)
//                .map(existing -> {
//                    if (req.title != null && !req.title.isBlank()) {
//                        existing.setTitle(req.title);
//                    }
//                    if (req.authorId != null) {
//                        Author author = authorRepo.findById(req.authorId).orElse(null);
//                        if (author == null) return ResponseEntity.<BookDto>badRequest().build();
//                        existing.setAuthor(author);
//                    }
//
//                    Book saved = bookRepo.save(existing);
//
//                    BookDto dto = new BookDto();
//                    dto.id = saved.getId();
//                    dto.title = saved.getTitle();
//                    dto.authorId = saved.getAuthor().getId();
//                    dto.authorName = saved.getAuthor().getName();
//
//                    return ResponseEntity.ok(dto);
//                })
//                .orElseGet(() -> ResponseEntity.<BookDto>notFound().build());
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable Long id) {
//        if (!bookRepo.existsById(id)) return ResponseEntity.notFound().build();
//        bookRepo.deleteById(id);
//        return ResponseEntity.noContent().build();
//    }
//}
//
//
////package com.example.demo.controller;
////
////import com.example.demo.entity.Author;
////import com.example.demo.entity.Book;
////import com.example.demo.repository.AuthorRepository;
////import com.example.demo.repository.BookRepository;
////import org.springframework.http.ResponseEntity;
////import org.springframework.web.bind.annotation.*;
////
////import java.util.List;
////
////@CrossOrigin(origins = "http://localhost:4200")
////@RestController
////@RequestMapping("/api/books")
////public class BookController {
////
////    private final BookRepository bookRepo;
////    private final AuthorRepository authorRepo;
////
////    public BookController(BookRepository bookRepo, AuthorRepository authorRepo) {
////        this.bookRepo = bookRepo;
////        this.authorRepo = authorRepo;
////    }
////
////    // DTO for create/update
////    public static class BookRequest {
////        public String title;
////        public Long authorId;
////    }
////
////    @GetMapping
////    public List<Book> all() {
////        return bookRepo.findAll();
////    }
////
////    @GetMapping("/{id}")
////    public ResponseEntity<Book> one(@PathVariable Long id) {
////        return bookRepo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
////    }
////
////    @PostMapping
////    public ResponseEntity<Book> create(@RequestBody BookRequest req) {
////        if (req.title == null || req.title.isBlank() || req.authorId == null) {
////            return ResponseEntity.badRequest().build();
////        }
////
////        Author author = authorRepo.findById(req.authorId).orElse(null);
////        if (author == null) return ResponseEntity.badRequest().build();
////
////        Book b = new Book();
////        b.setTitle(req.title);
////        b.setAuthor(author);
////
////        return ResponseEntity.ok(bookRepo.save(b));
////    }
////
//////    @PutMapping("/{id}")
//////    public ResponseEntity<Book> update(@PathVariable Long id, @RequestBody BookRequest req) {
//////        return bookRepo.findById(id).map(existing -> {
//////            if (req.title != null && !req.title.isBlank()) {
//////                existing.setTitle(req.title);
//////            }
//////            if (req.authorId != null) {
//////                Author author = authorRepo.findById(req.authorId).orElse(null);
//////                if (author == null) return ResponseEntity.badRequest().build();
//////                existing.setAuthor(author);
//////            }
//////            return ResponseEntity.ok(bookRepo.save(existing));
//////        }).orElseGet(() -> ResponseEntity.<Book>notFound().build());
//////    }
////
////    @DeleteMapping("/{id}")
////    public ResponseEntity<Void> delete(@PathVariable Long id) {
////        if (!bookRepo.existsById(id)) return ResponseEntity.notFound().build();
////        bookRepo.deleteById(id);
////        return ResponseEntity.noContent().build();
////    }
////}
