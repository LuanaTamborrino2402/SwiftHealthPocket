package com.luanatamborrino.SwiftHealthPocket.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "Struttura")
@Table(name = "struttura")
public class Struttura {

    @Id
    @Column(name = "id", updatable = false)
    @SequenceGenerator(name = "struttura_sequence", sequenceName = "struttura_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "struttura_sequence")
    private Long id;

    @Column(name = "nome", nullable = false, columnDefinition = "VARCHAR(50)")
    private String nome;

    @Column(name = "indirizzo", nullable = false, columnDefinition = "VARCHAR(50)")
    private String indirizzo;

    @Column(name = "cap", nullable = false, length = 5)
    private Integer cap;

    @OneToMany(mappedBy = "struttura", fetch = FetchType.LAZY)
    private List<Utente> infermieri; //Lista di infermieri assegnati ad una struttura.

    @OneToMany(mappedBy = "struttura", fetch = FetchType.LAZY)
    private List<Prestazione> prestazioni; //Lista di prestazioni associate ad una struttura.
}
