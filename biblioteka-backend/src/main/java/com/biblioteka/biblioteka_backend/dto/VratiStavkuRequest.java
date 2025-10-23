package com.biblioteka.biblioteka_backend.dto;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class VratiStavkuRequest {
    private LocalDate datumVracanja;
    private Integer statusId;
    private Integer poeni;
    private Double kazna;
}
