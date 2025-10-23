// repo/AutorRepo.java
package com.biblioteka.biblioteka_backend.repo;

import com.biblioteka.biblioteka_backend.model.Autor;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AutorRepo extends JpaRepository<Autor, Integer> {

    @Query("""
      select a from Autor a
      where lower(a.ime) like lower(concat('%', :term, '%'))
         or lower(a.prezime) like lower(concat('%', :term, '%'))
         or lower(concat(a.ime,' ',a.prezime)) like lower(concat('%', :term, '%'))
    """)
    List<Autor> search(@Param("term") String term);
}
