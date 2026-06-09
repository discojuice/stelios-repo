package com.example.demo.controller;

import com.example.demo.entity.BlogPost;
import com.example.demo.repository.BlogPostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BlogPostControllerTest {

    @Mock
    private BlogPostRepository blogPostRepository;

    @InjectMocks
    private BlogPostController blogPostController;

    private MockMvc mockMvc;
    private BlogPost testBlogPost;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(blogPostController).build();

        testBlogPost = new BlogPost();
        testBlogPost.setId(1L);
        testBlogPost.setTitle("Test Blog Post");
        testBlogPost.setContent("This is test content");
        testBlogPost.setMediaUrl("http://example.com/image.jpg");
        testBlogPost.setMediaType("image/jpeg");
        testBlogPost.setCreatedOn(LocalDateTime.now());
    }

    @Test
    void testGetAllPosts() throws Exception {
        List<BlogPost> posts = List.of(testBlogPost);
        when(blogPostRepository.findAll()).thenReturn(posts);

        mockMvc.perform(get("/api/blog-posts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Blog Post"));

        verify(blogPostRepository, times(1)).findAll();
    }

    @Test
    void testGetAllPosts_Empty() throws Exception {
        when(blogPostRepository.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/blog-posts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(blogPostRepository, times(1)).findAll();
    }

    @Test
    void testGetAllPosts_Multiple() throws Exception {
        BlogPost post2 = new BlogPost();
        post2.setId(2L);
        post2.setTitle("Another Blog Post");
        post2.setContent("More content");
        post2.setCreatedOn(LocalDateTime.now());

        List<BlogPost> posts = List.of(testBlogPost, post2);
        when(blogPostRepository.findAll()).thenReturn(posts);

        mockMvc.perform(get("/api/blog-posts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("Test Blog Post"))
                .andExpect(jsonPath("$[1].title").value("Another Blog Post"));

        verify(blogPostRepository, times(1)).findAll();
    }
}