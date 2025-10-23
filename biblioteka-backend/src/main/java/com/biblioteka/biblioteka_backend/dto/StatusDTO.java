package com.biblioteka.biblioteka_backend.dto;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StatusDTO {
    private Integer id;
    private String naziv; // AKTIVNA/VRACENA/ZAKASNELA
}
