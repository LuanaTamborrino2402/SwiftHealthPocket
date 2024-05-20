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

/**
 * Handler responsabile per la gestione(selezione ed esecuzione) delle strategie di ricerca delle prestazioni.
 */
@Component
@RequiredArgsConstructor
public class CercaPrestazioneHandler {

    private final PrestazioneService prestazioneService;

    /**
     * Metodo per selezionare la strategia di ricerca appropriata in base al tipo di prestazione.
     * @param tipoPrestazione Il tipo di prestazione per cui si deve scegliere una strategia.
     * @return Un'istanza di CercaPrestazioneStrategy specifica per il tipo di prestazione dato.
     */
    public CercaPrestazioneStrategy scegliStrategy(String tipoPrestazione) {

        //Verifico che la stringa tipoPrestazione non sia vuota o nulla.
        Methods.getInstance().checkStringData(List.of(tipoPrestazione));

        //Variabile locale per tenere traccia della strategia selezionata.
        CercaPrestazioneStrategy strategy;

        //Selezione della strategia basata sul valore di tipoPrestazione.
        if(tipoPrestazione.equals("TAMPONE")){
            strategy = new CercaTampone(prestazioneService);
        }else if(tipoPrestazione.equals("VACCINO")){
            strategy = new CercaVaccino(prestazioneService);
        }else{
            //Lancio l'eccezione se il tipo di prestazione non è riconosciuto.
            throw new BadRequestException("Tipo di prestazione non valaido:" + tipoPrestazione);
        }

        //Ritorno la strategia.
        return strategy;
    }

    /**
     * Metodo per eseguire la strategia di ricerca fornita
     * @param strategy La strategia di ricerca da eseguire.
     * @return Lista di DTO con i risultati della ricerca.
     */
    public List<PrestazioneResponse> eseguiStrategy(CercaPrestazioneStrategy strategy){

        //Eseguo
        List<Prestazione> prestazioni = strategy.cercaPrestazioni();

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
