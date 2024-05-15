package com.luanatamborrino.SwiftHealthPocket.strategy.implementation;

import com.luanatamborrino.SwiftHealthPocket.model.Prestazione;
import com.luanatamborrino.SwiftHealthPocket.service.PrestazioneService;
import com.luanatamborrino.SwiftHealthPocket.strategy.CercaPrestazioneStrategy;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CercaTampone implements CercaPrestazioneStrategy {

    private final PrestazioneService prestazioneService;
    @Override
    public List<Prestazione> cercaPrestazioni(){

        List<Prestazione> listaTamponi = prestazioneService.cercaTampone();

        return listaTamponi;
    }
}
