package com.biblioteka.biblioteka_backend.dto;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PozajmicaCreateReq {
    @NotNull private Integer clanId;
    @NotNull private LocalDate datumPozajmice;
    @NotNull private LocalDate datumRokaPozajmice;
    @NotNull @Size(min=1) private List<StavkaCreateReq> stavke;
}
