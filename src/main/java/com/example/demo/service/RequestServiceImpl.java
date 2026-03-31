package com.example.demo.service;

import com.example.demo.dto.RequestDto;
import com.example.demo.entity.Request;
import com.example.demo.repository.RequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    public RequestServiceImpl(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public List<RequestDto> getAll() {
        return requestRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public RequestDto getById(Long id) {
        return requestRepository.findById(id)
                .map(this::toDto)
                .orElse(null);
    }

    @Override
    public List<RequestDto> search(String question, String answer) {
        List<Request> results;

        boolean hasQuestion = question != null && !question.isBlank();
        boolean hasAnswer = answer != null && !answer.isBlank();

        if (hasQuestion && hasAnswer) {
            results = requestRepository
                    .findByQuestionContainingIgnoreCaseAndAnswerContainingIgnoreCase(question, answer);
        } else if (hasQuestion) {
            results = requestRepository.findByQuestionContainingIgnoreCase(question);
        } else if (hasAnswer) {
            results = requestRepository.findByAnswerContainingIgnoreCase(answer);
        } else {
            results = requestRepository.findAll();
        }

        return results.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public RequestDto create(RequestDto input) {
        Request entity = new Request();
        entity.setQuestion(input.getQuestion());
        entity.setAnswer(input.getAnswer());
        entity.setCreatedOn();
        entity.setDepartment(input.getDepartment());
        entity.setCategory(input.getCategory());
        entity.setRequestNo(input.getRequestNo());

        Request saved = requestRepository.save(entity);
        return toDto(saved);
    }

    @Override
    public RequestDto update(Long id, RequestDto input) {
        return requestRepository.findById(id)
                .map(existing -> {
                    existing.setQuestion(input.getQuestion());
                    existing.setAnswer(input.getAnswer());
                    Request saved = requestRepository.save(existing);
                    return toDto(saved);
                })
                .orElse(null);
    }

    @Override
    public boolean delete(Long id) {
        if (!requestRepository.existsById(id)) {
            return false;
        }
        requestRepository.deleteById(id);
        return true;
    }

    private RequestDto toDto(Request entity) {
        RequestDto dto = new RequestDto();
        dto.setId(entity.getId());
        dto.setQuestion(entity.getQuestion());
        dto.setAnswer(entity.getAnswer());
        dto.setCreatedOn(entity.getCreatedOn());
        dto.setCategory(entity.getCategory());
        dto.setDepartment(entity.getDepartment());
        dto.setRequestNo((entity.getRequestNo()));
        return dto;
    }

}
