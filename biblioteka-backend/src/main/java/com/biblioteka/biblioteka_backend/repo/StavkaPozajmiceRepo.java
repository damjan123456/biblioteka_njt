package com.biblioteka.biblioteka_backend.repo;

import com.biblioteka.biblioteka_backend.model.StavkaPozajmice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface StavkaPozajmiceRepo extends JpaRepository<StavkaPozajmice, Integer> {

    @Query("""
      select (count(s) > 0) from StavkaPozajmice s
      join s.pozajmica p
      where s.knjiga.id in :bookIds
        and p.datumRokaPozajmice >= :start
        and p.datumPozajmice   <= :end
        and (s.status.naziv <> 'VRACENA' or s.datumVracanja is null)
    """)
    boolean existsOverlappingAny(@Param("bookIds") List<Integer> bookIds,
                                 @Param("start") LocalDate start,
                                 @Param("end") LocalDate end);

    @Query("""
      select (count(s) > 0) from StavkaPozajmice s
      join s.pozajmica p
      where s.knjiga.id = :bookId
        and p.datumRokaPozajmice >= :start
        and p.datumPozajmice   <= :end
        and (s.status.naziv <> 'VRACENA' or s.datumVracanja is null)
    """)
    boolean existsOverlapping(@Param("bookId") Integer bookId,
                              @Param("start") LocalDate start,
                              @Param("end") LocalDate end);

}
