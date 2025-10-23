package com.biblioteka.biblioteka_backend.dto;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StavkaDTO {
    private Integer id;
    private Integer knjigaId;
    private Integer statusId;
    private Integer brojPoena;
    private String kazna;
    private LocalDate datumVracanja;
    private String knjigaNaslov;
    private String statusNaziv;
}
