package com.luanatamborrino.SwiftHealthPocket.strategy.implementation;

import com.luanatamborrino.SwiftHealthPocket.model.Prestazione;
import com.luanatamborrino.SwiftHealthPocket.service.PrestazioneService;
import com.luanatamborrino.SwiftHealthPocket.strategy.CercaPrestazioneStrategy;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 *  Implementazione concreta dell'interfaccia CercaPrestazioneStrategy per la ricerca di vaccini.
 */
@RequiredArgsConstructor
public class CercaVaccino implements CercaPrestazioneStrategy {

    private final PrestazioneService prestazioneService;

    /**
     * Metodo che effettua una ricerca di vaccini attraverso il metodo cercaVaccino fornito da PrestazioneService.
     * @return Lista di oggetti Prestazione che rappresentano i vaccini trovati.
     */
    @Override
    public List<Prestazione> cercaPrestazioni() {

        //Chiamo il metodo cercaVaccino di PrestazioneService per ottenere la lista dei vaccini disponibili.
        List<Prestazione> listaVaccini = prestazioneService.cercaVaccino();

        //Ritorno la lista dei vaccini.
        return listaVaccini;
    }
}
