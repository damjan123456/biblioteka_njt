// src/main/java/com/biblioteka/biblioteka_backend/service/AutorService.java
package com.biblioteka.biblioteka_backend.service;

import com.biblioteka.biblioteka_backend.dto.AutorCreateReq;
import com.biblioteka.biblioteka_backend.dto.AutorDTO;
import com.biblioteka.biblioteka_backend.model.Autor;
import com.biblioteka.biblioteka_backend.repo.AutorRepo;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class AutorService {

    private final AutorRepo autorRepo;

    public AutorService(AutorRepo autorRepo) {
        this.autorRepo = autorRepo;
    }

    public List<Autor> searchAuthors(String name) {
        if (name == null || name.isBlank()) return autorRepo.findAll();
        return autorRepo.search(name.trim());
    }

    public Autor create(AutorCreateReq req) {
        var a = Autor.builder()
                .ime(req.getIme())
                .prezime(req.getPrezime())
                .build();
        return autorRepo.save(a);
    }

    public Autor update(Integer id, AutorCreateReq req) {
        var a = autorRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Autor nije pronađen"));
        a.setIme(req.getIme());
        a.setPrezime(req.getPrezime());
        return autorRepo.save(a);
    }

    public void delete(Integer id) {
        if (!autorRepo.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        autorRepo.deleteById(id);
    }


}
