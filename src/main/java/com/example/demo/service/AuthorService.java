package com.example.demo.service;

import com.example.demo.dto.AuthorDto;

import java.util.List;

public interface AuthorService {
    List<AuthorDto> getAll();
    AuthorDto create(AuthorDto input);
    AuthorDto update(Long id, AuthorDto input);   // return null if not found
    boolean delete(Long id);                      // true if deleted, false if not found
}
