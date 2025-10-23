package com.biblioteka.biblioteka_backend.web;

import com.biblioteka.biblioteka_backend.dto.ClanCreateReq;
import com.biblioteka.biblioteka_backend.dto.ClanDTO;
import com.biblioteka.biblioteka_backend.model.Clan;
import com.biblioteka.biblioteka_backend.model.Uloga;
import com.biblioteka.biblioteka_backend.repo.ClanRepo;
import com.biblioteka.biblioteka_backend.repo.UlogaRepo;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/clanovi")
public class ClanController {

    private final ClanRepo clanRepo;
    private final UlogaRepo ulogaRepo;

    public ClanController(ClanRepo clanRepo, UlogaRepo ulogaRepo) {
        this.clanRepo = clanRepo;
        this.ulogaRepo = ulogaRepo;
    }

    // Jedina dozvoljena operacija: registracija
    @PostMapping("/register")
    public ResponseEntity<ClanDTO> register(@Valid @RequestBody ClanCreateReq req) {
        if (clanRepo.existsByEmail(req.getEmail()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email zauzet");

        // dodeli rolu CLAN
        Uloga uloga = ulogaRepo.findByNaziv("CLAN")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Uloga CLAN ne postoji"));

        Clan c = Clan.builder()
                .ime(req.getIme())
                .prezime(req.getPrezime())
                .email(req.getEmail())
                .telefon(req.getTelefon())
                .adresa(req.getAdresa())
                .ukupnoPoena(0)
                .uloga(uloga)
                .password(req.getPassword() != null ? req.getPassword() : "changeme") // privremeno
                .build();

        c = clanRepo.save(c);

        ClanDTO d = new ClanDTO();
        d.setId(c.getId());
        d.setIme(c.getIme());
        d.setPrezime(c.getPrezime());
        d.setEmail(c.getEmail());
        d.setTelefon(c.getTelefon());
        d.setAdresa(c.getAdresa());
        d.setUkupnoPoena(c.getUkupnoPoena());
        d.setUloga(uloga.getNaziv());

        return ResponseEntity.status(HttpStatus.CREATED).body(d);
    }

    @GetMapping
    public ResponseEntity<List<ClanDTO>> list(@RequestParam(required = false) String q) {
        List<Clan> src = (q == null || q.isBlank()) ? clanRepo.findAll() : clanRepo.search(q.trim());
        List<ClanDTO> out = src.stream()
                .map(c -> ClanDTO.builder()
                        .id(c.getId()).ime(c.getIme()).prezime(c.getPrezime()).email(c.getEmail())
                        .build())
                .toList();
        return ResponseEntity.ok(out);
    }


}

