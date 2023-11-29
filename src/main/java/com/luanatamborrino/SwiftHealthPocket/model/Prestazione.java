package com.luanatamborrino.SwiftHealthPocket.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Prestazione")
@Table(name = "prestazione")
@Builder
public class Prestazione {

    @Id
    @Column(name = "id_Prestazione", updatable = false)
    @SequenceGenerator(name = "prestazione_sequence", sequenceName = "prestazione_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prestazione_sequence")
    private Long idPrestazione;

    @Column(name = "tipo_prestazione", nullable = false, columnDefinition = "VARCHAR(20)")
    @Enumerated(EnumType.STRING)
    private TipoPrestazione tipoPrestazione;

    @Column(name = "esito", nullable = false, columnDefinition = "VARCHAR(20)")
    @Enumerated(EnumType.STRING)
    private EsitoPrestazione esito;

    @Column(name = "data_inizio", nullable = false)
    private LocalDateTime dataInizio;

    @Column(name = "data_fine", nullable = false)
    private LocalDateTime dataFine;

    @ManyToOne
    @JoinColumn( name = "id_paziente")
    private Utente paziente;

    @ManyToOne
    @JoinColumn( name = "id_infermiere")
    private Utente infermiere;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_prestazione", referencedColumnName = "id_recensione")
    private Recensione recensione;

    @ManyToOne
    @JoinColumn(name = "id_struttura")
    private Struttura struttura;
}
