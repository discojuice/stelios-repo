package com.example.demo.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class EntityTest {

    @Test
    void testBlogPostEntity() {
        BlogPost blogPost = new BlogPost();
        blogPost.setId(1L);
        blogPost.setTitle("Test Post");
        blogPost.setContent("Test Content");
        blogPost.setMediaUrl("http://example.com/image.jpg");
        blogPost.setMediaType("image/jpeg");
        LocalDateTime now = LocalDateTime.now();
        blogPost.setCreatedOn(now);

        assertEquals(1L, blogPost.getId());
        assertEquals("Test Post", blogPost.getTitle());
        assertEquals("Test Content", blogPost.getContent());
        assertEquals("http://example.com/image.jpg", blogPost.getMediaUrl());
        assertEquals("image/jpeg", blogPost.getMediaType());
        assertEquals(now, blogPost.getCreatedOn());
    }

    @Test
    void testBlogCommentEntity() {
        BlogComment comment = new BlogComment();
        comment.setBlogPostId(1L);
        comment.setAuthorName("John Doe");
        comment.setCommentText("Nice post!");
        LocalDateTime now = LocalDateTime.now();
        comment.setCreatedOn(now);

        assertEquals(1L, comment.getBlogPostId());
        assertEquals("John Doe", comment.getAuthorName());
        assertEquals("Nice post!", comment.getCommentText());
        assertEquals(now, comment.getCreatedOn());
    }

    @Test
    void testRequestEntity() {
        Request request = new Request();
        request.setId(1L);
        request.setQuestion("What is Java?");
        request.setAnswer("Java is a language");
        request.setCategory("Programming");
        request.setDepartment("IT");
        request.setRequestNo("REQ-001");
        request.setCreatedOn();

        assertEquals(1L, request.getId());
        assertEquals("What is Java?", request.getQuestion());
        assertEquals("Java is a language", request.getAnswer());
        assertEquals("Programming", request.getCategory());
        assertEquals("IT", request.getDepartment());
        assertEquals("REQ-001", request.getRequestNo());
        assertNotNull(request.getCreatedOn());
    }

    @Test
    void testRequestEntity_CreatedOnIsSet() {
        Request request = new Request();
        assertNull(request.getCreatedOn());

        request.setCreatedOn();
        assertNotNull(request.getCreatedOn());
        assertTrue(request.getCreatedOn() instanceof Date);
    }
}