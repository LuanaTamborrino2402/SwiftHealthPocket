package com.luanatamborrino.SwiftHealthPocket.strategy.handler;

import com.luanatamborrino.SwiftHealthPocket.dto.response.PrestazioneResponse;
import com.luanatamborrino.SwiftHealthPocket.exception.BadRequestException;
import com.luanatamborrino.SwiftHealthPocket.model.Prestazione;
import com.luanatamborrino.SwiftHealthPocket.service.PrestazioneService;
import com.luanatamborrino.SwiftHealthPocket.strategy.CercaPrestazioneStrategy;
import com.luanatamborrino.SwiftHealthPocket.strategy.implementation.CercaTampone;
import com.luanatamborrino.SwiftHealthPocket.strategy.implementation.CercaVaccino;
import com.luanatamborrino.SwiftHealthPocket.util.Methods;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CercaPrestazioneHandler {

    private final PrestazioneService prestazioneService;

    public CercaPrestazioneStrategy scegliStrategy(String tipoPrestazione){

        Methods.getInstance().checkStringData(List.of(tipoPrestazione));

        CercaPrestazioneStrategy strategy;

        if(tipoPrestazione.equals("TAMPONE")){
            strategy = new CercaTampone(prestazioneService);
        }else if(tipoPrestazione.equals("VACCINO")){
            strategy = new CercaVaccino(prestazioneService);
        }else{
            throw new BadRequestException("Prestazione non valida");
        }

        return strategy;
    }

    public List<PrestazioneResponse> eseguiStrategy(CercaPrestazioneStrategy cercaPrestazioneStrategy){

        List<Prestazione> prestazioni = cercaPrestazioneStrategy.cercaPrestazioni();

        List<PrestazioneResponse> response= new ArrayList<>();

        for(Prestazione prestazione: prestazioni){
            if(prestazione.getInfermiere() !=null || prestazione.getDataInizio().isAfter(LocalDateTime.now().plusDays(2))){
                response.add(new PrestazioneResponse(
                        prestazione.getIdPrestazione(),
                        prestazione.getTipoPrestazione(),
                        prestazione.getEsito(),
                        prestazione.getDataInizio(),
                        prestazione.getDataFine()
                ));
            }
        }

        return response;
    }

}
