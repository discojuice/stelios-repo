package com.example.demo.controller;

import com.example.demo.dto.RequestDto;
import com.example.demo.service.RequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(
        origins = {
                "http://localhost:4200",
                "https://myproject-1-vf3w.onrender.com"
        }
)
@RestController
@RequestMapping("/api/requests")
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping
    public List<RequestDto> all() {
        return requestService.getAll();
    }

    @GetMapping("/search")
    public List<RequestDto> search(
            @RequestParam(required = false) String question,
            @RequestParam(required = false) String answer) {
        return requestService.search(question, answer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequestDto> one(@PathVariable Long id) {
        RequestDto request = requestService.getById(id);
        if (request == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(request);
    }

    @PostMapping
    public ResponseEntity<RequestDto> create(@RequestBody RequestDto input) {
        RequestDto created = requestService.create(input);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RequestDto> update(@PathVariable Long id, @RequestBody RequestDto input) {
        RequestDto updated = requestService.update(id, input);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = requestService.delete(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}