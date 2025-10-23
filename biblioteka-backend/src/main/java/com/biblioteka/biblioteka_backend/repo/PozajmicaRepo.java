// src/main/java/com/biblioteka/biblioteka_backend/repo/PozajmnicaRepo.java
package com.biblioteka.biblioteka_backend.repo;

import com.biblioteka.biblioteka_backend.model.Pozajmica;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PozajmicaRepo extends JpaRepository<Pozajmica, Integer> {


    @Query("""
        select distinct p
        from Pozajmica p
        left join fetch p.clan
        left join fetch p.stavke s
        left join fetch s.knjiga
        left join fetch s.status
    """)
    List<Pozajmica> findAllWithRel();

    @Query("""
        select distinct p
        from Pozajmica p
        left join fetch p.clan
        left join fetch p.stavke s
        left join fetch s.knjiga
        left join fetch s.status
        where p.clan.id = :clanId
    """)
    List<Pozajmica> findAllWithRelByClanId(Integer clanId);
}
