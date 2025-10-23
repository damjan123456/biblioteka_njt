package com.biblioteka.biblioteka_backend.web;

import com.biblioteka.biblioteka_backend.model.Uloga;
import com.biblioteka.biblioteka_backend.repo.UlogaRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/uloge")
public class UlogaController {

    private final UlogaRepo repo;
    public UlogaController(UlogaRepo repo) { this.repo = repo; }

    @GetMapping
    public ResponseEntity<List<Uloga>> all() {
        return ResponseEntity.ok(repo.findAll());
    }
}
