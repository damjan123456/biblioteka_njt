package com.biblioteka.biblioteka_backend.dto;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class VratiStavkuRequest {
    @NotNull private LocalDate datumVracanja;
}
