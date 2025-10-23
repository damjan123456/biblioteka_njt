package com.biblioteka.biblioteka_backend.repo;

import com.biblioteka.biblioteka_backend.model.Knjiga;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface KnjigaRepo extends JpaRepository<Knjiga, Integer> {

    @Query("""
      select k from Knjiga k
      where lower(k.naslov) like lower(concat('%', :term, '%'))
         or lower(k.isbn)   like lower(concat('%', :term, '%'))
    """)
    List<Knjiga> search(@Param("term") String term);

    boolean existsByIsbn(String isbn);
}
