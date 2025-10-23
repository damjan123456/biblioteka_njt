package com.biblioteka.biblioteka_backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(
        name = "STAVKA_LOYALTY",
        uniqueConstraints = @UniqueConstraint(name = "uq_stavka_pravilo", columnNames = {"id_stavka","id_pravilo"})
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StavkaLoyalty {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aplikacije")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_stavka", nullable = false)
    private StavkaPozajmice stavka;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_pravilo", nullable = false)
    private LoyaltyPravilo pravilo;

    @Column(name = "poeni", nullable = false)  private Integer poeni;
    @Column(name = "napomena")                 private String napomena;

    @Column(name = "created_at", nullable = false, updatable = false,
            columnDefinition = "timestamp default current_timestamp")
    private Instant createdAt;
}

