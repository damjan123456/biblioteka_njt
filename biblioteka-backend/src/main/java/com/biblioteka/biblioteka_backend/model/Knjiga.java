package com.biblioteka.biblioteka_backend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;


@Entity @Table(name = "KNJIGA")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Knjiga {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_knjiga")
    private Integer id;

    @Column(name = "naslov", nullable = false, length = 255)     private String naslov;
    @Column(name = "isbn", nullable = false, unique = true, length = 32) private String isbn;
    @Column(name = "godina_izdanja")                             private int godinaIzdanja;
    @Column(name = "izdavac", length = 150)                      private String izdavac;
    @Column(name = "opis")                                       private String opis;
    @Column(name = "dostupna", nullable = false)                 private Boolean dostupna;

    @ManyToMany
    @JoinTable(name = "KNJIGA_AUTOR",
            joinColumns = @JoinColumn(name = "id_knjiga"),
            inverseJoinColumns = @JoinColumn(name = "id_autor"))
    @JsonManagedReference("knjiga-autori")
    private Set<Autor> autori = new HashSet<>();
}

