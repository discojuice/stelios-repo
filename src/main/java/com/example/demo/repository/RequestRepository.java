package com.example.demo.repository;

import com.example.demo.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findByQuestionContainingIgnoreCase(String question);

    List<Request> findByAnswerContainingIgnoreCase(String answer);

    List<Request> findByQuestionContainingIgnoreCaseAndAnswerContainingIgnoreCase(String question, String answer);
}