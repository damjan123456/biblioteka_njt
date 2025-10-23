package com.biblioteka.biblioteka_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "LOYALTYPRAVILO")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LoyaltyPravilo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id_pravilo")
    private Integer id;

    @Column(name = "naziv", nullable = false, length = 100)
    private String naziv;

    @Column(name = "opis")
    private String opis;

    @Column(name = "tip_obracuna", nullable = false, length = 50)
    private String tipObracuna;

    @Column(name = "vrednost_poena", nullable = false)
    private Integer vrednostPoena;
}

