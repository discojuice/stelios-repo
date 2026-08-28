package com.example.demo.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.demo.entity.BlogPost;
import com.example.demo.repository.BlogPostRepository;

@Service
public class BlogPostService {

    private final BlogPostRepository blogPostRepository;

    public BlogPostService(BlogPostRepository blogPostRepository) {
        this.blogPostRepository = blogPostRepository;
    }

    public List<BlogPost> getPostsPage(int page, int size) {
        List<Integer> groupIds = blogPostRepository.findDistinctGroupIds(
                PageRequest.of(page, size));

        if (groupIds.isEmpty()) {
            return List.of();
        }

        return blogPostRepository.findByGroupIdInOrderByCreatedOnDesc(groupIds);
    }
}