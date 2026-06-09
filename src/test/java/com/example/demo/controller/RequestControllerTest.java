package com.example.demo.controller;

import com.example.demo.dto.RequestDto;
import com.example.demo.service.RequestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RequestControllerTest {

    @Mock
    private RequestService requestService;

    @InjectMocks
    private RequestController requestController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private RequestDto testRequestDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(requestController).build();
        objectMapper = new ObjectMapper();

        testRequestDto = new RequestDto();
        testRequestDto.setId(1L);
        testRequestDto.setQuestion("What is Java?");
        testRequestDto.setAnswer("Java is a programming language");
        testRequestDto.setCategory("Programming");
        testRequestDto.setDepartment("IT");
        testRequestDto.setRequestNo("REQ-001");
        testRequestDto.setCreatedOn(new Date());
    }

    @Test
    void testGetAll() throws Exception {
        List<RequestDto> requests = List.of(testRequestDto);
        when(requestService.getAll()).thenReturn(requests);

        mockMvc.perform(get("/api/requests")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].question").value("What is Java?"));

        verify(requestService, times(1)).getAll();
    }

    @Test
    void testGetAll_Empty() throws Exception {
        when(requestService.getAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/requests")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(requestService, times(1)).getAll();
    }

    @Test
    void testSearch_WithQuestion() throws Exception {
        List<RequestDto> requests = List.of(testRequestDto);
        when(requestService.search("Java", null)).thenReturn(requests);

        mockMvc.perform(get("/api/requests/search")
                .param("question", "Java")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(requestService, times(1)).search("Java", null);
    }

    @Test
    void testSearch_WithAnswer() throws Exception {
        List<RequestDto> requests = List.of(testRequestDto);
        when(requestService.search(null, "programming")).thenReturn(requests);

        mockMvc.perform(get("/api/requests/search")
                .param("answer", "programming")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(requestService, times(1)).search(null, "programming");
    }

    @Test
    void testSearch_WithBoth() throws Exception {
        List<RequestDto> requests = List.of(testRequestDto);
        when(requestService.search("Java", "programming")).thenReturn(requests);

        mockMvc.perform(get("/api/requests/search")
                .param("question", "Java")
                .param("answer", "programming")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(requestService, times(1)).search("Java", "programming");
    }

    @Test
    void testSearch_NoParams() throws Exception {
        List<RequestDto> requests = List.of(testRequestDto);
        when(requestService.search(null, null)).thenReturn(requests);

        mockMvc.perform(get("/api/requests/search")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(requestService, times(1)).search(null, null);
    }

    @Test
    void testGetById_Success() throws Exception {
        when(requestService.getById(1L)).thenReturn(testRequestDto);

        mockMvc.perform(get("/api/requests/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.question").value("What is Java?"));

        verify(requestService, times(1)).getById(1L);
    }

    @Test
    void testGetById_NotFound() throws Exception {
        when(requestService.getById(999L)).thenReturn(null);

        mockMvc.perform(get("/api/requests/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(requestService, times(1)).getById(999L);
    }

    @Test
    void testCreate_Success() throws Exception {
        RequestDto createdDto = new RequestDto();
        createdDto.setId(1L);
        createdDto.setQuestion("New Question?");
        createdDto.setAnswer("New Answer");
        createdDto.setCategory("Test");
        createdDto.setDepartment("IT");
        createdDto.setRequestNo("REQ-002");

        when(requestService.create(any(RequestDto.class))).thenReturn(createdDto);

        String requestBody = objectMapper.writeValueAsString(createdDto);

        mockMvc.perform(post("/api/requests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.question").value("New Question?"));

        verify(requestService, times(1)).create(any(RequestDto.class));
    }

    @Test
    void testUpdate_Success() throws Exception {
        RequestDto updateDto = new RequestDto();
        updateDto.setQuestion("Updated Question?");
        updateDto.setAnswer("Updated Answer");

        RequestDto updatedDto = new RequestDto();
        updatedDto.setId(1L);
        updatedDto.setQuestion("Updated Question?");
        updatedDto.setAnswer("Updated Answer");

        when(requestService.update(eq(1L), any(RequestDto.class))).thenReturn(updatedDto);

        String requestBody = objectMapper.writeValueAsString(updateDto);

        mockMvc.perform(put("/api/requests/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.question").value("Updated Question?"));

        verify(requestService, times(1)).update(eq(1L), any(RequestDto.class));
    }

    @Test
    void testUpdate_NotFound() throws Exception {
        RequestDto updateDto = new RequestDto();
        updateDto.setQuestion("Updated Question?");

        when(requestService.update(eq(999L), any(RequestDto.class))).thenReturn(null);

        String requestBody = objectMapper.writeValueAsString(updateDto);

        mockMvc.perform(put("/api/requests/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isNotFound());

        verify(requestService, times(1)).update(eq(999L), any(RequestDto.class));
    }

    @Test
    void testDelete_Success() throws Exception {
        when(requestService.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/requests/1"))
                .andExpect(status().isNoContent());

        verify(requestService, times(1)).delete(1L);
    }

    @Test
    void testDelete_NotFound() throws Exception {
        when(requestService.delete(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/requests/999"))
                .andExpect(status().isNotFound());

        verify(requestService, times(1)).delete(999L);
    }
}