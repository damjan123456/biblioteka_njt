package com.biblioteka.biblioteka_backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity @Table(name = "AUTOR")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Autor {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_autor")
    private Integer id;

    @Column(name = "ime", nullable = false, length = 100)     private String ime;
    @Column(name = "prezime", nullable = false, length = 100) private String prezime;

    @ManyToMany(mappedBy = "autori")
    @JsonBackReference("knjiga-autori")
    private Set<Knjiga> knjige = new HashSet<>();
}

