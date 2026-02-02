package com.example.demo.service;

import com.example.demo.dto.AuthorDto;
import com.example.demo.entity.Author;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
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
        if (input == null || input.name == null || input.name.trim().isEmpty()) {
            throw new BadRequestException("Author name is required");
        }

        Author a = new Author();
        a.setName(input.name.trim());
        return toDto(authorRepo.save(a));
    }

    @Override
    public AuthorDto update(Long id, AuthorDto input) {
        if (id == null) {
            throw new BadRequestException("Author id is required");
        }
        if (input == null || input.name == null || input.name.trim().isEmpty()) {
            throw new BadRequestException("Author name is required");
        }

        Author existing = authorRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Author not found: " + id));

        existing.setName(input.name.trim());
        return toDto(authorRepo.save(existing));
    }

    @Override
    public boolean delete(Long id) {
        if (id == null) {
            throw new BadRequestException("Author id is required");
        }

        Author existing = authorRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Author not found: " + id));

        authorRepo.delete(existing);
        return true;
    }

    private AuthorDto toDto(Author a) {
        AuthorDto dto = new AuthorDto();
        dto.id = a.getId();
        dto.name = a.getName();
        return dto;
    }
}
