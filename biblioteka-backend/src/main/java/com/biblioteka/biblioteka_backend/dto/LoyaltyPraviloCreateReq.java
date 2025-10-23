package com.biblioteka.biblioteka_backend.dto;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LoyaltyPraviloCreateReq {
    @NotBlank @Size(max=100) private String naziv;
    private String opis;
    @NotBlank @Size(max=50) private String tipObracuna;
    @NotNull @Min(0) private Integer vrednostPoena;
}
