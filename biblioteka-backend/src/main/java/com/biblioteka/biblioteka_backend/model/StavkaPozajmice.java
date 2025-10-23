package com.biblioteka.biblioteka_backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "STAVKAPOZAJMICE")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StavkaPozajmice {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_stavka")
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_pozajmica", nullable = false)
    @JsonBackReference("pozajmica-stavke")
    private Pozajmica pozajmica;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_knjiga", nullable = false)
    private Knjiga knjiga;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_status", nullable = false)
    private StatusPozajmice status;

    @Column(name = "broj_poena", nullable = false) private Integer brojPoena;
    @Column(name = "kazna", nullable = false)      private BigDecimal kazna;
    @Column(name = "datum_vracanja")               private LocalDate datumVracanja;

    @OneToMany(mappedBy = "stavka", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<StavkaLoyalty> loyaltyZapisi = new ArrayList<>();
}

