package com.biblioteka.biblioteka_backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity @Table(name = "CLAN")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Clan {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_clan")
    private Integer id;

    @Column(name = "ime", nullable = false, length = 100)
    private String ime;

    @Column(name = "prezime", nullable = false, length = 100)
    private String prezime;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "telefon", length = 50)
    private String telefon;

    @Column(name = "adresa", length = 255)
    private String adresa;

    @Column(name = "datum_uclanjenja", nullable = false)
    private LocalDate datumUclanjenja;

    @Column(name = "ukupno_poena", nullable = false)
    private Integer ukupnoPoena;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_uloga", nullable = false)
    private Uloga uloga;

    @OneToMany(mappedBy="clan")
    private List<Pozajmica> pozajmice;
}

