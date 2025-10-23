// src/main/java/com/biblioteka/biblioteka_backend/service/KnjigaService.java
package com.biblioteka.biblioteka_backend.service;

import com.biblioteka.biblioteka_backend.dto.KnjigaDTO;
import com.biblioteka.biblioteka_backend.model.Autor;
import com.biblioteka.biblioteka_backend.model.Knjiga;
import com.biblioteka.biblioteka_backend.repo.AutorRepo;
import com.biblioteka.biblioteka_backend.repo.KnjigaRepo;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;

@Service
@Transactional
public class KnjigaService {

    private final KnjigaRepo knjigaRepo;
    private final AutorRepo autorRepo;

    public KnjigaService(KnjigaRepo knjigaRepo, AutorRepo autorRepo) {
        this.knjigaRepo = knjigaRepo;
        this.autorRepo = autorRepo;
    }

    public List<Knjiga> searchBooks(String term) {
        if (term == null || term.isBlank()) return knjigaRepo.findAll();
        return knjigaRepo.search(term);
    }

    public Knjiga createBook(KnjigaDTO dto) {
        if (knjigaRepo.existsByIsbn(dto.getIsbn()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "ISBN već postoji");

        var ids = dto.getAutorIds();
        if (ids == null || ids.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Potrebno je navesti autore");

        var autori = new HashSet<>(autorRepo.findAllById(ids));
        if (autori.size() != ids.size())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Autor nije pronađen");

        Knjiga k = Knjiga.builder()
                .naslov(dto.getNaslov())
                .isbn(dto.getIsbn())
                .godinaIzdanja(dto.getGodinaIzdanja())
                .izdavac(dto.getIzdavac())
                .opis(dto.getOpis())
                .dostupna(Boolean.TRUE.equals(dto.getDostupna()))
                .autori(autori)
                .build();

        return knjigaRepo.save(k);
    }

    public Knjiga updateBook(Integer id, KnjigaDTO dto) {
        Knjiga existing = knjigaRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Knjiga nije pronađena"));

        if (!existing.getIsbn().equals(dto.getIsbn()) && knjigaRepo.existsByIsbn(dto.getIsbn()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "ISBN već postoji");

        var ids = dto.getAutorIds();
        if (ids == null || ids.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Potrebno je navesti autore");

        var autori = new HashSet<>(autorRepo.findAllById(ids));
        if (autori.size() != ids.size())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Autor nije pronađen");

        existing.setNaslov(dto.getNaslov());
        existing.setIsbn(dto.getIsbn());
        existing.setGodinaIzdanja(dto.getGodinaIzdanja());
        existing.setIzdavac(dto.getIzdavac());
        existing.setOpis(dto.getOpis());
        existing.setDostupna(Boolean.TRUE.equals(dto.getDostupna()));
        existing.setAutori(autori);

        return knjigaRepo.save(existing);
    }

    public void deleteBook(Integer id) {
        if (!knjigaRepo.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Knjiga nije pronađena");
        knjigaRepo.deleteById(id);
    }
}

