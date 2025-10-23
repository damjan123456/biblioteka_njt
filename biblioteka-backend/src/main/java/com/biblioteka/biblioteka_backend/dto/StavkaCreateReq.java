package com.biblioteka.biblioteka_backend.dto;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StavkaCreateReq {
    @NotNull private Integer knjigaId;
    @NotNull private Integer statusId; // npr. AKTIVNA
}
