package com.biblioteka.biblioteka_backend.dto;

import lombok.*;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class KnjigaViewDTO {
    private Integer id;
    private String naslov;
    private String isbn;
    private Integer godinaIzdanja;
    private String izdavac;
    private String opis;
    private Boolean dostupna;   // stanje u bazi
    private boolean available;  // izračunato za danas (nema preklapanja)
    private Set<Integer> autorIds;
}

