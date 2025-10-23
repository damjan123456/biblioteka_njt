package com.biblioteka.biblioteka_backend.repo;

import com.biblioteka.biblioteka_backend.model.Clan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClanRepo extends JpaRepository<Clan, Integer> {
    Optional<Clan> findByEmail(String email);
    boolean existsByEmail(String email);
}
