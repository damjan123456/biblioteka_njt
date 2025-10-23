package com.biblioteka.biblioteka_backend.dto;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class KnjigaCreateReq {
    @NotBlank @Size(max=255) private String naslov;
    @NotBlank @Size(max=32)  private String isbn;
    private LocalDate godinaIzdanja;
    @Size(max=150) private String izdavac;
    private String opis;
    private Boolean dostupna;
    @NotNull private Set<@NotNull Integer> autorIds;
}

