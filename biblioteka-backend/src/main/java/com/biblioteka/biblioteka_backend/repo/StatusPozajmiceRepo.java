package com.biblioteka.biblioteka_backend.repo;

import com.biblioteka.biblioteka_backend.model.StatusPozajmice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusPozajmiceRepo extends JpaRepository<StatusPozajmice, Integer> {
    Optional<StatusPozajmice> findByNaziv(String naziv); // "AKTIVNA","VRACENA","ZAKASNELA"
}

