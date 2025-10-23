package com.biblioteka.biblioteka_backend.dto;
import lombok.*;
import java.time.LocalDate;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class KnjigaDTO {
    private Integer id;
    private String naslov;
    private String isbn;
    private int godinaIzdanja;
    private String izdavac;
    private String opis;
    private Boolean dostupna;
    private Set<Integer> autorIds;
}

