// src/main/java/com/biblioteka/biblioteka_backend/service/LoyaltyPraviloService.java
package com.biblioteka.biblioteka_backend.service;

import com.biblioteka.biblioteka_backend.dto.LoyaltyPraviloCreateReq;
import com.biblioteka.biblioteka_backend.model.LoyaltyPravilo;
import com.biblioteka.biblioteka_backend.repo.LoyaltyPraviloRepo;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class LoyaltyPraviloService {

    private final LoyaltyPraviloRepo repo;
    public LoyaltyPraviloService(LoyaltyPraviloRepo repo) { this.repo = repo; }

    public List<LoyaltyPravilo> searchRules(String term) {
        if (term == null || term.isBlank()) return repo.findAll();
        return repo.search(term.trim());
    }

    public LoyaltyPravilo create(LoyaltyPraviloCreateReq req) {
        var p = LoyaltyPravilo.builder()
                .naziv(req.getNaziv())
                .opis(req.getOpis())
                .tipObracuna(req.getTipObracuna())      // "po_knjizi" | "bonus_na_vreme"
                .vrednostPoena(req.getVrednostPoena())
                .build();
        return repo.save(p);
    }

    public LoyaltyPravilo update(Integer id, LoyaltyPraviloCreateReq req) {
        var p = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pravilo nije pronađeno"));
        p.setNaziv(req.getNaziv());
        p.setOpis(req.getOpis());
        p.setTipObracuna(req.getTipObracuna());
        p.setVrednostPoena(req.getVrednostPoena());
        return repo.save(p);
    }

    public void delete(Integer id) {
        if (!repo.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        repo.deleteById(id);
    }
}
