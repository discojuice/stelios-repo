package com.example.demo.service;

import com.example.demo.dto.BookCreateUpdateRequest;
import com.example.demo.dto.BookDto;

import java.util.List;

public interface BookService {
    List<BookDto> getAll();
    BookDto create(BookCreateUpdateRequest req);      // return null if bad authorId / bad input
    BookDto update(Long id, BookCreateUpdateRequest req); // return null if not found, or throw/return null on bad authorId
    boolean delete(Long id);
}