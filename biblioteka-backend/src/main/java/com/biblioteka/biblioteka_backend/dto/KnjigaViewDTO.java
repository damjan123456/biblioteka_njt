package com.biblioteka.biblioteka_backend.dto;

import lombok.*;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class KnjigaViewDTO {
    private Integer id;
    private String naslov;
    private String isbn;
    private Integer godinaIzdanja;
    private String izdavac;
    private String opis;
    private Boolean dostupna;
    private boolean available;
    private List<String> autori;
}

