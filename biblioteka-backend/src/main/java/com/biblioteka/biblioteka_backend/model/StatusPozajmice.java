package com.biblioteka.biblioteka_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "STATUSPOZAJMICE")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StatusPozajmice {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_status")
    private Integer id;

    @Column(name = "naziv", nullable = false, unique = true, length = 50)
    private String naziv;
}
