package com.biblioteka.biblioteka_backend.dto;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LoyaltyPraviloDTO {
    private Integer id;
    private String naziv;
    private String opis;
    private String tipObracuna;   // po_knjizi, bonus_na_vreme
    private Integer vrednostPoena;
}
