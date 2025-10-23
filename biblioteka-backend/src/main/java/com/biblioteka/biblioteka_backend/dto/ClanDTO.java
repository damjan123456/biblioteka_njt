package com.biblioteka.biblioteka_backend.dto;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ClanDTO {
    private Integer id;
    private String ime;
    private String prezime;
    private String email;
    private String telefon;
    private String adresa;
    private Integer ukupnoPoena;
    private LocalDate datumUclanjenja;
    private String uloga;
}
