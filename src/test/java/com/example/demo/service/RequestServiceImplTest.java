package com.example.demo.service;

import com.example.demo.dto.RequestDto;
import com.example.demo.entity.Request;
import com.example.demo.repository.RequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {

    @Mock
    private RequestRepository requestRepository;

    @InjectMocks
    private RequestServiceImpl requestService;

    private Request testRequest;
    private RequestDto testRequestDto;

    @BeforeEach
    void setUp() {
        testRequest = new Request();
        testRequest.setId(1L);
        testRequest.setQuestion("What is Java?");
        testRequest.setAnswer("Java is a programming language");
        testRequest.setCategory("Programming");
        testRequest.setDepartment("IT");
        testRequest.setRequestNo("REQ-001");
        testRequest.setCreatedOn(new Date());

        testRequestDto = new RequestDto();
        testRequestDto.setId(1L);
        testRequestDto.setQuestion("What is Java?");
        testRequestDto.setAnswer("Java is a programming language");
        testRequestDto.setCategory("Programming");
        testRequestDto.setDepartment("IT");
        testRequestDto.setRequestNo("REQ-001");
        testRequestDto.setCreatedOn(testRequest.getCreatedOn());
    }

    @Test
    void testGetAll() {
        List<Request> requestList = List.of(testRequest);
        when(requestRepository.findAll()).thenReturn(requestList);

        List<RequestDto> result = requestService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("What is Java?", result.get(0).getQuestion());
        verify(requestRepository, times(1)).findAll();
    }

    @Test
    void testGetAll_Empty() {
        when(requestRepository.findAll()).thenReturn(new ArrayList<>());

        List<RequestDto> result = requestService.getAll();

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(requestRepository, times(1)).findAll();
    }

    @Test
    void testGetById_Success() {
        when(requestRepository.findById(1L)).thenReturn(Optional.of(testRequest));

        RequestDto result = requestService.getById(1L);

        assertNotNull(result);
        assertEquals("What is Java?", result.getQuestion());
        assertEquals(1L, result.getId());
        verify(requestRepository, times(1)).findById(1L);
    }

    @Test
    void testGetById_NotFound() {
        when(requestRepository.findById(999L)).thenReturn(Optional.empty());

        RequestDto result = requestService.getById(999L);

        assertNull(result);
        verify(requestRepository, times(1)).findById(999L);
    }

    @Test
    void testSearch_ByQuestion() {
        List<Request> results = List.of(testRequest);
        when(requestRepository.findByQuestionContainingIgnoreCase("Java")).thenReturn(results);

        List<RequestDto> result = requestService.search("Java", null);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(requestRepository, times(1)).findByQuestionContainingIgnoreCase("Java");
    }

    @Test
    void testSearch_ByAnswer() {
        List<Request> results = List.of(testRequest);
        when(requestRepository.findByAnswerContainingIgnoreCase("programming language")).thenReturn(results);

        List<RequestDto> result = requestService.search(null, "programming language");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(requestRepository, times(1)).findByAnswerContainingIgnoreCase("programming language");
    }

    @Test
    void testSearch_ByQuestionAndAnswer() {
        List<Request> results = List.of(testRequest);
        when(requestRepository.findByQuestionContainingIgnoreCaseAndAnswerContainingIgnoreCase("Java", "language"))
                .thenReturn(results);

        List<RequestDto> result = requestService.search("Java", "language");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(requestRepository, times(1)).findByQuestionContainingIgnoreCaseAndAnswerContainingIgnoreCase("Java", "language");
    }

    @Test
    void testSearch_NoParams() {
        List<Request> results = List.of(testRequest);
        when(requestRepository.findAll()).thenReturn(results);

        List<RequestDto> result = requestService.search(null, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(requestRepository, times(1)).findAll();
    }

    @Test
    void testSearch_EmptyStringParams() {
        List<Request> results = List.of(testRequest);
        when(requestRepository.findAll()).thenReturn(results);

        List<RequestDto> result = requestService.search("  ", "  ");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(requestRepository, times(1)).findAll();
    }

    @Test
    void testCreate_Success() {
        when(requestRepository.save(any(Request.class))).thenReturn(testRequest);

        RequestDto result = requestService.create(testRequestDto);

        assertNotNull(result);
        assertEquals("What is Java?", result.getQuestion());
        assertEquals("Programming", result.getCategory());
        verify(requestRepository, times(1)).save(any(Request.class));
    }

    @Test
    void testUpdate_Success() {
        RequestDto updateDto = new RequestDto();
        updateDto.setQuestion("What is Spring Boot?");
        updateDto.setAnswer("Spring Boot is a framework");
        updateDto.setCategory("Framework");
        updateDto.setDepartment("IT");

        Request updatedRequest = new Request();
        updatedRequest.setId(1L);
        updatedRequest.setQuestion("What is Spring Boot?");
        updatedRequest.setAnswer("Spring Boot is a framework");
        updatedRequest.setCategory("Framework");
        updatedRequest.setDepartment("IT");
        updatedRequest.setRequestNo("REQ-001");

        when(requestRepository.findById(1L)).thenReturn(Optional.of(testRequest));
        when(requestRepository.save(any(Request.class))).thenReturn(updatedRequest);

        RequestDto result = requestService.update(1L, updateDto);

        assertNotNull(result);
        assertEquals("What is Spring Boot?", result.getQuestion());
        verify(requestRepository, times(1)).findById(1L);
        verify(requestRepository, times(1)).save(any(Request.class));
    }

    @Test
    void testUpdate_NotFound() {
        RequestDto updateDto = new RequestDto();
        when(requestRepository.findById(999L)).thenReturn(Optional.empty());

        RequestDto result = requestService.update(999L, updateDto);

        assertNull(result);
        verify(requestRepository, times(1)).findById(999L);
        verify(requestRepository, never()).save(any(Request.class));
    }

    @Test
    void testDelete_Success() {
        when(requestRepository.existsById(1L)).thenReturn(true);

        boolean result = requestService.delete(1L);

        assertTrue(result);
        verify(requestRepository, times(1)).existsById(1L);
        verify(requestRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete_NotFound() {
        when(requestRepository.existsById(999L)).thenReturn(false);

        boolean result = requestService.delete(999L);

        assertFalse(result);
        verify(requestRepository, times(1)).existsById(999L);
        verify(requestRepository, never()).deleteById(any());
    }
}