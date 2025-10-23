package com.biblioteka.biblioteka_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "ULOGA")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Uloga {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_uloga")
    private Integer id;

    @Column(name = "naziv", nullable = false, unique = true, length = 50)
    private String naziv;
}

