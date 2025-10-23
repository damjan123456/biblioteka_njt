package com.biblioteka.biblioteka_backend.repo;

import com.biblioteka.biblioteka_backend.model.Clan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClanRepo extends JpaRepository<Clan, Integer> {
    Optional<Clan> findByEmail(String email);
    boolean existsByEmail(String email);
    @Query("""
       select c from Clan c
       where lower(c.ime) like lower(concat('%', :q, '%'))
          or lower(c.prezime) like lower(concat('%', :q, '%'))
          or lower(c.email) like lower(concat('%', :q, '%'))
    """)
    List<Clan> search(@Param("q") String q);
}
