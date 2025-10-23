// src/main/java/com/biblioteka/biblioteka_backend/web/PozajmnicaController.java
package com.biblioteka.biblioteka_backend.web;

import com.biblioteka.biblioteka_backend.dto.PozajmicaCreateReq;
import com.biblioteka.biblioteka_backend.dto.PozajmicaDTO;
import com.biblioteka.biblioteka_backend.dto.StavkaDTO;
import com.biblioteka.biblioteka_backend.model.Pozajmica;
import com.biblioteka.biblioteka_backend.model.StavkaPozajmice;
import com.biblioteka.biblioteka_backend.service.PozajmicaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/pozajmice")
public class PozajmicaController {

    private final PozajmicaService service;

    public PozajmicaController(PozajmicaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<PozajmicaDTO>> getPozajmice() {
        List<Pozajmica> list = service.getAllPozajmice(null); // bez filtera po clanu
        List<PozajmicaDTO> out = new ArrayList<>();

        for (Pozajmica p : list) {
            List<StavkaDTO> stavke = new ArrayList<>();
            for (StavkaPozajmice s : p.getStavke()) {
                stavke.add(StavkaDTO.builder()
                        .id(s.getId())
                        .knjigaId(s.getKnjiga().getId())
                        .statusId(s.getStatus().getId())
                        .brojPoena(s.getBrojPoena())
                        .kazna(s.getKazna() != null ? s.getKazna().toPlainString() : "0")
                        .datumVracanja(s.getDatumVracanja())
                        .build());
            }

            out.add(PozajmicaDTO.builder()
                    .id(p.getId())
                    .clanId(p.getClan().getId())
                    .datumPozajmice(p.getDatumPozajmice())
                    .datumRokaPozajmice(p.getDatumRokaPozajmice())
                    .ukupanBrojPoena(p.getUkupanBrojPoena())
                    .ukupnaKazna(p.getUkupnaKazna() != null ? p.getUkupnaKazna().toPlainString() : "0")
                    .stavke(stavke)
                    .build());
        }
        return ResponseEntity.ok(out);
    }

    @PostMapping
    public ResponseEntity<PozajmicaDTO> createPozajmnica(@Valid @RequestBody PozajmicaCreateReq dto) {
        Pozajmica p = service.createPozajmica(dto);

        List<StavkaDTO> stavke = new ArrayList<>();
        for (StavkaPozajmice s : p.getStavke()) {
            stavke.add(StavkaDTO.builder()
                    .id(s.getId())
                    .knjigaId(s.getKnjiga().getId())
                    .statusId(s.getStatus().getId())
                    .brojPoena(s.getBrojPoena())
                    .kazna(s.getKazna() != null ? s.getKazna().toPlainString() : "0")
                    .datumVracanja(s.getDatumVracanja())
                    .build());
        }

        PozajmicaDTO odg = PozajmicaDTO.builder()
                .id(p.getId())
                .clanId(p.getClan().getId())
                .datumPozajmice(p.getDatumPozajmice())
                .datumRokaPozajmice(p.getDatumRokaPozajmice())
                .ukupanBrojPoena(p.getUkupanBrojPoena())
                .ukupnaKazna(p.getUkupnaKazna() != null ? p.getUkupnaKazna().toPlainString() : "0")
                .stavke(stavke)
                .build();

        return ResponseEntity.ok(odg);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePozajmnica(@PathVariable Integer id) {
        service.deletePozajmnica(id);
        return ResponseEntity.noContent().build();
    }
}
