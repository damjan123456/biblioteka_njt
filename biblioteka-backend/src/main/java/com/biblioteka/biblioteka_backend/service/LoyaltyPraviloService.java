package com.biblioteka.biblioteka_backend.service;

import com.biblioteka.biblioteka_backend.model.LoyaltyPravilo;
import com.biblioteka.biblioteka_backend.repo.LoyaltyPraviloRepo;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Set;

@Service
@Transactional
public class LoyaltyPraviloService {
    private final LoyaltyPraviloRepo repo;
    public LoyaltyPraviloService(LoyaltyPraviloRepo repo) { this.repo = repo; }

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<LoyaltyPravilo> searchRules(String term) {
        if (term == null || term.isBlank()) return repo.findAll();
        return repo.search(term.trim());
    }

}

