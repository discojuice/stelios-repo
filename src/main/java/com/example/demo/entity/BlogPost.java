package com.example.demo.entity;

//public class BlogPost {
//}

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "blog_posts")
public class BlogPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String mediaUrl;

    private String mediaType;

    private LocalDateTime createdOn;

    private int groupId;
}