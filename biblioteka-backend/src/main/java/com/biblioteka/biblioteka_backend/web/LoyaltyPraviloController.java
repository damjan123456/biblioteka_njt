// src/main/java/com/biblioteka/biblioteka_backend/web/LoyaltyPraviloController.java
package com.biblioteka.biblioteka_backend.web;

import com.biblioteka.biblioteka_backend.dto.LoyaltyPraviloCreateReq;
import com.biblioteka.biblioteka_backend.model.LoyaltyPravilo;
import com.biblioteka.biblioteka_backend.service.LoyaltyPraviloService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loyalty")
public class LoyaltyPraviloController {

    private final LoyaltyPraviloService service;
    public LoyaltyPraviloController(LoyaltyPraviloService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<List<LoyaltyPravilo>> search(@RequestParam(required = false) String term) {
        return ResponseEntity.ok(service.searchRules(term));
    }

}

