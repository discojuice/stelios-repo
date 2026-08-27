package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "requests")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "question", columnDefinition = "TEXT")
    private String question;

    @Column(name = "answer", columnDefinition = "TEXT")
    private String answer;

    @Column(name = "category")
    private String category;

    @Column(name = "department")
    private String department;

    @Column(name = "request_no")
    private String requestNo;

    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    public void setCreatedOn() {
        this.createdOn = new Date();
    }
}