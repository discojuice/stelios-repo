package com.example.demo.controller;

import com.example.demo.dto.AuthorDto;
import com.example.demo.service.AuthorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public List<AuthorDto> all() {
        return authorService.getAll();
    }

    @PostMapping
    public AuthorDto create(@RequestBody AuthorDto input) {
        return authorService.create(input);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDto> update(@PathVariable Long id, @RequestBody AuthorDto input) {
        AuthorDto updated = authorService.update(id, input);
        return updated == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = authorService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}


//package com.example.demo.controller;
//
//import com.example.demo.dto.AuthorDto;
//import com.example.demo.entity.Author;
//import com.example.demo.repository.AuthorRepository;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@CrossOrigin(origins = "http://localhost:4200")
//@RestController
//@RequestMapping("/api/authors")
//public class AuthorController {
//
//    private final AuthorRepository authorRepo;
//
//    public AuthorController(AuthorRepository authorRepo) {
//        this.authorRepo = authorRepo;
//    }
//
//    @GetMapping
//    public List<AuthorDto> all() {
//        return authorRepo.findAll().stream().map(a -> {
//            AuthorDto dto = new AuthorDto();
//            dto.id = a.getId();
//            dto.name = a.getName();
//            return dto;
//        }).toList();
//    }
//
//    @PostMapping
//    public AuthorDto create(@RequestBody AuthorDto input) {
//        Author a = new Author();
//        a.setName(input.name);
//        a = authorRepo.save(a);
//
//        AuthorDto dto = new AuthorDto();
//        dto.id = a.getId();
//        dto.name = a.getName();
//        return dto;
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<AuthorDto> update(@PathVariable Long id, @RequestBody AuthorDto input) {
//        return authorRepo.findById(id)
//                .map(existing -> {
//                    existing.setName(input.name);
//                    Author saved = authorRepo.save(existing);
//
//                    AuthorDto dto = new AuthorDto();
//                    dto.id = saved.getId();
//                    dto.name = saved.getName();
//                    return ResponseEntity.ok(dto);
//                })
//                .orElseGet(() -> ResponseEntity.<AuthorDto>notFound().build());
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable Long id) {
//        if (!authorRepo.existsById(id)) return ResponseEntity.notFound().build();
//        authorRepo.deleteById(id);
//        return ResponseEntity.noContent().build();
//    }
//}
//
//


