package com.luanatamborrino.SwiftHealthPocket.strategy;

import com.luanatamborrino.SwiftHealthPocket.model.Prestazione;

import java.util.List;

/**
 * Interfaccia del pattern Strategy. Espone la firma del metodo cercaPrestazioni chiamato per cercare tipi diversi di prestazioni.
 * Ogni implementazione di questa interfaccia definisce i criteri specifici di ricerca basati su diversi tipi di prestazione.
 */
public interface CercaPrestazioneStrategy {

    /**
     * Esegue la ricerca di prestazioni specifiche, implementata secondo il tipo di prestazione configurato nella strategia concreta.
     * @return Lista di oggetti Prestazione che rappresentano le prestazioni sanitarie trovate.
     */
    List<Prestazione> cercaPrestazioni();
}
