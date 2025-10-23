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
    public ResponseEntity<List<KnjigaViewDTO>> listBooks(
            @RequestParam(name = "q", required = false) String q
    ) {
        LocalDate today = LocalDate.now();

        List<Knjiga> books = knjigaService.searchBooks(q);
        List<KnjigaViewDTO> response = new ArrayList<>();

        for (Knjiga k : books) {
            boolean reservedToday = stavkaRepo.existsOverlapping(k.getId(), today, today);
            boolean available = Boolean.TRUE.equals(k.getDostupna()) && !reservedToday;

            List<String> imena = (k.getAutori()==null) ? List.of()
                    : k.getAutori().stream()
                    .map(a -> a.getIme() + " " + a.getPrezime())
                    .toList();

            response.add(KnjigaViewDTO.builder()
                    .id(k.getId())
                    .naslov(k.getNaslov())
                    .isbn(k.getIsbn())
                    .godinaIzdanja(k.getGodinaIzdanja())
                    .izdavac(k.getIzdavac())
                    .opis(k.getOpis())
                    .dostupna(k.getDostupna())
                    .available(available)
                    .autori(imena)
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
