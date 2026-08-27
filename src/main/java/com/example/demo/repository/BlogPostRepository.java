package com.example.demo.repository;

import java.util.List;
import com.example.demo.entity.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

        List<BlogPost> findAllByOrderByCreatedOnDesc();

}