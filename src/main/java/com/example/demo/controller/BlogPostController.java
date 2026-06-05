package com.example.demo.controller;

import com.example.demo.entity.BlogPost;
import com.example.demo.repository.BlogPostRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/blog-posts")
@CrossOrigin(origins = "http://localhost:4200")
public class BlogPostController {

    private final BlogPostRepository repository;

    public BlogPostController(BlogPostRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<BlogPost> getAllPosts() {
        return repository.findAll();
    }
}
