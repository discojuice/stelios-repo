package com.example.demo.controller;

import com.example.demo.entity.BlogComment;
import com.example.demo.repository.BlogCommentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BlogCommentControllerTest {

    @Mock
    private BlogCommentRepository blogCommentRepository;

    @InjectMocks
    private BlogCommentController blogCommentController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private BlogComment testComment;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(blogCommentController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        testComment = new BlogComment();
        testComment.setBlogPostId(1L);
        testComment.setAuthorName("John Doe");
        testComment.setCommentText("Great post!");
        testComment.setCreatedOn(LocalDateTime.now());
    }

    @Test
    void testGetComments() throws Exception {
        List<BlogComment> comments = List.of(testComment);
        when(blogCommentRepository.findByBlogPostIdOrderByCreatedOnDesc(1L)).thenReturn(comments);

        mockMvc.perform(get("/api/blog-posts/1/comments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].authorName").value("John Doe"))
                .andExpect(jsonPath("$[0].commentText").value("Great post!"))
                .andExpect(jsonPath("$[0].blogPostId").value(1));

        verify(blogCommentRepository, times(1)).findByBlogPostIdOrderByCreatedOnDesc(1L);
    }

    @Test
    void testGetComments_Empty() throws Exception {
        when(blogCommentRepository.findByBlogPostIdOrderByCreatedOnDesc(1L)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/blog-posts/1/comments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(blogCommentRepository, times(1)).findByBlogPostIdOrderByCreatedOnDesc(1L);
    }

    @Test
    void testGetComments_Multiple() throws Exception {
        BlogComment comment2 = new BlogComment();
        comment2.setBlogPostId(1L);
        comment2.setAuthorName("Jane Smith");
        comment2.setCommentText("Thanks for sharing!");
        comment2.setCreatedOn(LocalDateTime.now().minusHours(1));

        List<BlogComment> comments = List.of(testComment, comment2);
        when(blogCommentRepository.findByBlogPostIdOrderByCreatedOnDesc(1L)).thenReturn(comments);

        mockMvc.perform(get("/api/blog-posts/1/comments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].authorName").value("John Doe"))
                .andExpect(jsonPath("$[1].authorName").value("Jane Smith"));

        verify(blogCommentRepository, times(1)).findByBlogPostIdOrderByCreatedOnDesc(1L);
    }

    @Test
    void testGetComments_DifferentPost() throws Exception {
        BlogComment comment = new BlogComment();
        comment.setBlogPostId(5L);
        comment.setAuthorName("Bob Johnson");
        comment.setCommentText("Excellent content!");
        comment.setCreatedOn(LocalDateTime.now());

        List<BlogComment> comments = List.of(comment);
        when(blogCommentRepository.findByBlogPostIdOrderByCreatedOnDesc(5L)).thenReturn(comments);

        mockMvc.perform(get("/api/blog-posts/5/comments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].blogPostId").value(5));

        verify(blogCommentRepository, times(1)).findByBlogPostIdOrderByCreatedOnDesc(5L);
    }

    @Test
    void testCreateComment_Success() throws Exception {
        BlogComment savedComment = new BlogComment();
        savedComment.setBlogPostId(1L);
        savedComment.setAuthorName("John Doe");
        savedComment.setCommentText("Great post!");
        savedComment.setCreatedOn(LocalDateTime.now());

        when(blogCommentRepository.save(any(BlogComment.class))).thenReturn(savedComment);

        BlogComment inputComment = new BlogComment();
        inputComment.setAuthorName("John Doe");
        inputComment.setCommentText("Great post!");

        String requestBody = objectMapper.writeValueAsString(inputComment);

        mockMvc.perform(post("/api/blog-posts/1/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authorName").value("John Doe"))
                .andExpect(jsonPath("$.commentText").value("Great post!"))
                .andExpect(jsonPath("$.blogPostId").value(1));

        verify(blogCommentRepository, times(1)).save(any(BlogComment.class));
    }

    @Test
    void testCreateComment_SetsBlogPostIdFromPath() throws Exception {
        ArgumentCaptor<BlogComment> argumentCaptor = ArgumentCaptor.forClass(BlogComment.class);

        BlogComment savedComment = new BlogComment();
        savedComment.setBlogPostId(5L);
        savedComment.setAuthorName("Author");
        savedComment.setCommentText("Comment text");
        savedComment.setCreatedOn(LocalDateTime.now());

        when(blogCommentRepository.save(any(BlogComment.class))).thenReturn(savedComment);

        BlogComment inputComment = new BlogComment();
        inputComment.setAuthorName("Author");
        inputComment.setCommentText("Comment text");

        String requestBody = objectMapper.writeValueAsString(inputComment);

        mockMvc.perform(post("/api/blog-posts/5/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.blogPostId").value(5));

        verify(blogCommentRepository, times(1)).save(argumentCaptor.capture());
        assertEquals(5L, argumentCaptor.getValue().getBlogPostId());
    }

    @Test
    void testCreateComment_SetsCreatedOn() throws Exception {
        ArgumentCaptor<BlogComment> argumentCaptor = ArgumentCaptor.forClass(BlogComment.class);

        BlogComment savedComment = new BlogComment();
        savedComment.setBlogPostId(1L);
        savedComment.setAuthorName("Test Author");
        savedComment.setCommentText("Test Comment");
        LocalDateTime now = LocalDateTime.now();
        savedComment.setCreatedOn(now);

        when(blogCommentRepository.save(any(BlogComment.class))).thenReturn(savedComment);

        BlogComment inputComment = new BlogComment();
        inputComment.setAuthorName("Test Author");
        inputComment.setCommentText("Test Comment");

        String requestBody = objectMapper.writeValueAsString(inputComment);

        mockMvc.perform(post("/api/blog-posts/1/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.createdOn").exists());

        verify(blogCommentRepository, times(1)).save(argumentCaptor.capture());
        assertNotNull(argumentCaptor.getValue().getCreatedOn());
    }

    @Test
    void testCreateComment_MultipleComments() throws Exception {
        BlogComment savedComment = new BlogComment();
        savedComment.setBlogPostId(1L);
        savedComment.setAuthorName("User 1");
        savedComment.setCommentText("First comment");
        savedComment.setCreatedOn(LocalDateTime.now());

        when(blogCommentRepository.save(any(BlogComment.class))).thenReturn(savedComment);

        BlogComment inputComment = new BlogComment();
        inputComment.setAuthorName("User 1");
        inputComment.setCommentText("First comment");

        String requestBody = objectMapper.writeValueAsString(inputComment);

        // First comment
        mockMvc.perform(post("/api/blog-posts/1/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk());

        // Second comment
        inputComment.setAuthorName("User 2");
        inputComment.setCommentText("Second comment");
        String requestBody2 = objectMapper.writeValueAsString(inputComment);

        mockMvc.perform(post("/api/blog-posts/1/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody2))
                .andExpect(status().isOk());

        verify(blogCommentRepository, times(2)).save(any(BlogComment.class));
    }
}