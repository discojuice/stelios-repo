package com.example.demo.repository;

import com.example.demo.entity.BlogComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogCommentRepository extends JpaRepository<BlogComment, Long> {

    List<BlogComment> findByBlogPostIdOrderByCreatedOnDesc(Long blogPostId);
}