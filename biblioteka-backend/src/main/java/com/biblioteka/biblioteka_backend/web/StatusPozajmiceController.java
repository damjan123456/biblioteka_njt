package com.biblioteka.biblioteka_backend.web;

import com.biblioteka.biblioteka_backend.model.StatusPozajmice;
import com.biblioteka.biblioteka_backend.repo.StatusPozajmiceRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statusi-pozajmice")
public class StatusPozajmiceController {

    private final StatusPozajmiceRepo repo;
    public StatusPozajmiceController(StatusPozajmiceRepo repo) { this.repo = repo; }

    @GetMapping
    public ResponseEntity<List<StatusPozajmice>> all() {
        return ResponseEntity.ok(repo.findAll());
    }
}
