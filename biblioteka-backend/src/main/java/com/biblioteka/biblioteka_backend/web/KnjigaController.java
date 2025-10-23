// src/main/java/com/biblioteka/biblioteka_backend/web/KnjigaController.java
package com.biblioteka.biblioteka_backend.web;

import com.biblioteka.biblioteka_backend.dto.KnjigaDTO;
import com.biblioteka.biblioteka_backend.dto.KnjigaViewDTO;
import com.biblioteka.biblioteka_backend.model.Autor;
import com.biblioteka.biblioteka_backend.model.Knjiga;
import com.biblioteka.biblioteka_backend.repo.StavkaPozajmiceRepo;
import com.biblioteka.biblioteka_backend.service.KnjigaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/knjige")
public class KnjigaController {

    private final KnjigaService knjigaService;
    private final StavkaPozajmiceRepo stavkaRepo;

    public KnjigaController(KnjigaService knjigaService, StavkaPozajmiceRepo stavkaRepo) {
        this.knjigaService = knjigaService;
        this.stavkaRepo = stavkaRepo;
    }

    @GetMapping
    public ResponseEntity<List<KnjigaViewDTO>> listBooks() {
        LocalDate today = LocalDate.now();

        List<Knjiga> books = knjigaService.searchBooks(null);
        List<KnjigaViewDTO> response = new ArrayList<>();

        for (Knjiga k : books) {
            boolean reservedToday = stavkaRepo.existsOverlapping(k.getId(), today, today);
            boolean available = Boolean.TRUE.equals(k.getDostupna()) && !reservedToday;

            Set<Integer> autorIds = k.getAutori()==null ? Set.of()
                    : k.getAutori().stream().map(Autor::getId).collect(Collectors.toSet());

            response.add(KnjigaViewDTO.builder()
                    .id(k.getId())
                    .naslov(k.getNaslov())
                    .isbn(k.getIsbn())
                    .godinaIzdanja(k.getGodinaIzdanja())
                    .izdavac(k.getIzdavac())
                    .opis(k.getOpis())
                    .dostupna(k.getDostupna())
                    .available(available)
                    .autorIds(autorIds)
                    .build());
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Knjiga> create(@RequestBody @Valid KnjigaDTO dto) {
        return ResponseEntity.ok(knjigaService.createBook(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Knjiga> update(@PathVariable Integer id, @RequestBody @Valid KnjigaDTO dto) {
        return ResponseEntity.ok(knjigaService.updateBook(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        knjigaService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
