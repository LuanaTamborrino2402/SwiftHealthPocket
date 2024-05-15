package com.luanatamborrino.SwiftHealthPocket.strategy.implementation;

import com.luanatamborrino.SwiftHealthPocket.model.Prestazione;
import com.luanatamborrino.SwiftHealthPocket.service.PrestazioneService;
import com.luanatamborrino.SwiftHealthPocket.strategy.CercaPrestazioneStrategy;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CercaVaccino implements CercaPrestazioneStrategy {

    private final PrestazioneService prestazioneService;
    @Override
    public List<Prestazione> cercaPrestazioni(){

        List<Prestazione> listaVaccini = prestazioneService.cercaVaccino();

        return listaVaccini;
    }
}
