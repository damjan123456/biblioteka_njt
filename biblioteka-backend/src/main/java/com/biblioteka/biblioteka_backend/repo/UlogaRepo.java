package com.biblioteka.biblioteka_backend.repo;

import com.biblioteka.biblioteka_backend.model.Uloga;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UlogaRepo extends JpaRepository<Uloga, Integer> {
    Optional<Uloga> findByNaziv(String naziv);
}
