package com.example.demo.controller;

import com.example.demo.entity.BlogComment;
import com.example.demo.repository.BlogCommentRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/blog-posts/{postId}/comments")
@CrossOrigin(origins = "http://localhost:4200")
public class BlogCommentController {

    private final BlogCommentRepository repository;

    public BlogCommentController(BlogCommentRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<BlogComment> getComments(@PathVariable Long postId) {
        return repository.findByBlogPostIdOrderByCreatedOnDesc(postId);
    }

    @PostMapping
    public BlogComment createComment(
            @PathVariable Long postId,
            @RequestBody BlogComment comment
    ) {
        comment.setBlogPostId(postId);
        comment.setCreatedOn(LocalDateTime.now());
        return repository.save(comment);
    }
}