package com.biblioteka.biblioteka_backend.dto;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AutorCreateReq {
    @NotBlank @Size(max=100) private String ime;
    @NotBlank @Size(max=100) private String prezime;
}
