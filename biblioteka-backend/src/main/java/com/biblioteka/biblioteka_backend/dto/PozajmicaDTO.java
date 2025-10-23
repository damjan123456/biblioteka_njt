package com.biblioteka.biblioteka_backend.dto;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PozajmicaDTO {
    private Integer id;
    private Integer clanId;
    private LocalDate datumPozajmice;
    private LocalDate datumRokaPozajmice;
    private String ukupnaKazna;
    private Integer ukupanBrojPoena;
    private List<StavkaDTO> stavke;
}
