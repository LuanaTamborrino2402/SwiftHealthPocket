package com.luanatamborrino.SwiftHealthPocket.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Recensione")
@Table(name = "recensione")
@Builder
public class Recensione {

    @Id
    @Column(name = "id_recensione", updatable = false)
    @SequenceGenerator(name = "recensione_sequence", sequenceName = "recensione_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recensione_sequence")
    private Long idRecensione;

    @Column(name = "valutazione", nullable = false)
    @Min(1)
    @Max(5)
    private Integer valutazione;

    @Column(name = "commento", nullable = false, columnDefinition = "VARCHAR(1000)")
    private String commento;

    @Column(name = "data", nullable = false)
    private LocalDateTime data;

    @ManyToOne
    @JoinColumn(name = "id_utente")
    private Utente paziente; //Paziente che recensisce.

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_recensione", referencedColumnName = "id_prestazione")
    private Prestazione prestazione; //Prestazione recensita.
}
