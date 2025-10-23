package com.biblioteka.biblioteka_backend.web;

import com.biblioteka.biblioteka_backend.dto.AutorCreateReq;
import com.biblioteka.biblioteka_backend.model.Autor;
import com.biblioteka.biblioteka_backend.service.AutorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/autori")
public class AutorController {

    private final AutorService service;
    public AutorController(AutorService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<List<Autor>> search(@RequestParam(required = false) String name) {
        return ResponseEntity.ok(service.searchAuthors(name));
    }

    @PostMapping
    public ResponseEntity<Autor> create(@Valid @RequestBody AutorCreateReq req) {
        return ResponseEntity.ok(service.create(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Autor> update(@PathVariable Integer id, @Valid @RequestBody AutorCreateReq req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

