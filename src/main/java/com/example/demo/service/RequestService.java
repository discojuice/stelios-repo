package com.example.demo.service;

import com.example.demo.dto.RequestDto;
import com.example.demo.repository.RequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;


public interface RequestService {

    List<RequestDto> getAll();

    RequestDto getById(Long id);

    List<RequestDto> search(String question, String answer);

    RequestDto create(RequestDto input);

    RequestDto update(Long id, RequestDto input);

    boolean delete(Long id);
}