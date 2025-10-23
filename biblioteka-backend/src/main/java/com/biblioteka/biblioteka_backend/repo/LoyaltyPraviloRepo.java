package com.biblioteka.biblioteka_backend.repo;

import com.biblioteka.biblioteka_backend.model.LoyaltyPravilo;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LoyaltyPraviloRepo extends JpaRepository<LoyaltyPravilo, Integer> {
    Optional<LoyaltyPravilo> findByTipObracuna(String tipObracuna); // "po_knjizi","bonus_na_vreme"

    @Query("""
      select p from LoyaltyPravilo p
      where lower(p.naziv) like lower(concat('%', :term, '%'))
         or lower(p.opis)  like lower(concat('%', :term, '%'))
         or lower(p.tipObracuna) like lower(concat('%', :term, '%'))
    """)
    List<LoyaltyPravilo> search(@Param("term") String term);
}

