package com.luanatamborrino.SwiftHealthPocket.service;

import com.luanatamborrino.SwiftHealthPocket.dto.request.CreaStrutturaRequest;
import com.luanatamborrino.SwiftHealthPocket.dto.response.StrutturaResponse;
import com.luanatamborrino.SwiftHealthPocket.dto.response.UserResponse;
import com.luanatamborrino.SwiftHealthPocket.exception.BadRequestException;
import com.luanatamborrino.SwiftHealthPocket.exception.NotFoundException;
import com.luanatamborrino.SwiftHealthPocket.model.Struttura;
import com.luanatamborrino.SwiftHealthPocket.model.Utente;
import com.luanatamborrino.SwiftHealthPocket.repository.StrutturaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StrutturaService {

    private final StrutturaRepository repository;

    public void creaStruttura(CreaStrutturaRequest request){

        //Controllo se almeno un campo è vuoto.
        if(request.getNome().isEmpty() || request.getNome().isBlank() ||
                request.getCap() == null ||
                request.getIndirizzo().isEmpty() || request.getIndirizzo().isBlank()){
            throw new BadRequestException("Campo non inserito.");
        }

        //Controllo se il valore del campo "Cap" non è esattamente di 5 caratteri e lancio un'eccezione in caso contrario.
        if(String.valueOf(request.getCap()).length() != 5 ) {
            throw new BadRequestException("Cap non valido.");
        }

        //Creo una struttura.
        Struttura struttura = Struttura.builder()
                .nome(request.getNome())
                .cap(request.getCap())
                .indirizzo(request.getIndirizzo())
                .build();

        //Salvo la struttura creata nel database.
        repository.save(struttura);
    }

    public StrutturaResponse getStrutturaData(Long id){

        //controllo che l'id parta da 1.
        if(id < 1) {
            throw new BadRequestException("Id non corretto.");
        }

        //Controllo se è presente una struttura con quell'id.
        Optional<Struttura> struttura = repository.findById(id);

        //Se non viene trovata alcua struttura con l'ID fornito, viene lanciata l'eccezione.
        if(struttura.isEmpty()){
            throw new NotFoundException("Struttura non trovata.");
        }

        //Se esiste, viene creato e resituito il nuovo oggetto Struttura.
        return new StrutturaResponse(struttura.get().getId(),
                struttura.get().getNome(),
                struttura.get().getIndirizzo(),
                struttura.get().getCap());
    }

    public List<StrutturaResponse> getAllStrutture(){

        //Prendo tutte le strutture dal database.
        List<Struttura> structureList = repository.findAll();

        if(structureList.isEmpty()){
            throw new NotFoundException("Strutture non trovate.");
        }

        //Creo la lists di strutture.
        List<StrutturaResponse> response = new ArrayList<>();

        //Per ogni struttura trovata, creo un oggetto StrutturaResponse e lo aggiungo alla lista.
        for (Struttura struttura : structureList ){
            response.add(
                new StrutturaResponse(
                    struttura.getId(),
                    struttura.getNome(),
                    struttura.getIndirizzo(),
                    struttura.getCap()
                )
            );

        }

        //Restituisco la lista di strutture.
        return response;
    }
}
