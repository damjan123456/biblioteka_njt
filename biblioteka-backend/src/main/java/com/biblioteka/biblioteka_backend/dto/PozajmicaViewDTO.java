package com.biblioteka.biblioteka_backend.dto;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PozajmicaViewDTO {
    private Integer id;
    private Integer clanId;
    private String clanIme;          // "Ime Prezime"
    private LocalDate datumPozajmice;
    private LocalDate datumRokaPozajmice;
    private Integer brojStavki;
    private Integer ukupanBrojPoena;
    private String ukupnaKazna;      // formatiran BigDecimal
}

