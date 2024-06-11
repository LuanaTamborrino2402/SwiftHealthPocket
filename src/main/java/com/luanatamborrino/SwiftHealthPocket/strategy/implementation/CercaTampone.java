package com.luanatamborrino.SwiftHealthPocket.strategy.implementation;

import com.luanatamborrino.SwiftHealthPocket.model.Prestazione;
import com.luanatamborrino.SwiftHealthPocket.service.PrestazioneService;
import com.luanatamborrino.SwiftHealthPocket.strategy.CercaPrestazioneStrategy;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Implementazione concreta dell'interfaccia CercaPrestazioneStrategy per la ricerca di tamponi.
 */
@RequiredArgsConstructor
public class CercaTampone implements CercaPrestazioneStrategy {

    private final PrestazioneService prestazioneService;

    @Override
    public List<Prestazione> cercaPrestazioni() {

        //Chiamo PrestazioneService per ottenere la lista dei tamponi disponibili.
        List<Prestazione> listaTamponi = prestazioneService.cercaTampone();

        //Ritorno la lista dei tamponi.
        return listaTamponi;
    }
}
