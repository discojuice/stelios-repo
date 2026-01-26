package com.example.demo.service;

import com.example.demo.dto.AuthorDto;
import com.example.demo.entity.Author;
import com.example.demo.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepo;

    public AuthorServiceImpl(AuthorRepository authorRepo) {
        this.authorRepo = authorRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorDto> getAll() {
        return authorRepo.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public AuthorDto create(AuthorDto input) {
        Author a = new Author();
        a.setName(input.name);
        return toDto(authorRepo.save(a));
    }

    @Override
    public AuthorDto update(Long id, AuthorDto input) {
        return authorRepo.findById(id)
                .map(existing -> {
                    existing.setName(input.name);
                    return toDto(authorRepo.save(existing));
                })
                .orElse(null);
    }

    @Override
    public boolean delete(Long id) {
        if (!authorRepo.existsById(id)) return false;
        authorRepo.deleteById(id);
        return true;
    }

    private AuthorDto toDto(Author a) {
        AuthorDto dto = new AuthorDto();
        dto.id = a.getId();
        dto.name = a.getName();
        return dto;
    }
}
