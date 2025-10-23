package com.biblioteka.biblioteka_backend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "POZAJMICA")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Pozajmica {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pozajmica")
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_clan", nullable = false)
    private Clan clan;

    @Column(name = "datum_pozajmice", nullable = false)
    private LocalDate datumPozajmice;

    @Column(name = "datum_roka_pozajmice", nullable = false)
    private LocalDate datumRokaPozajmice;

    @Column(name = "ukupna_kazna", nullable = false)
    private BigDecimal ukupnaKazna;

    @Column(name = "ukupan_broj_poena", nullable = false)
    private Integer ukupanBrojPoena;


    @OneToMany(mappedBy = "pozajmica", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("pozajmica-stavke")
    private List<StavkaPozajmice> stavke = new ArrayList<>();
}

