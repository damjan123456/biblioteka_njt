package com.biblioteka.biblioteka_backend.dto;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ClanCreateReq {
    @NotBlank @Size(max=100) private String ime;
    @NotBlank @Size(max=100) private String prezime;
    @NotBlank @Size(max=150) private String email;
    @NotBlank @Size(max=150) private String password;
    @Size(max=50) private String telefon;
    @Size(max=255) private String adresa;
    @NotNull private LocalDate datumUclanjenja;
    @NotNull private Integer ulogaId;
}
