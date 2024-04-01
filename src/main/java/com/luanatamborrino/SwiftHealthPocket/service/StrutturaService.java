package com.luanatamborrino.SwiftHealthPocket.service;

import com.luanatamborrino.SwiftHealthPocket.dto.request.AssociaDissociaInfermiereRequest;
import com.luanatamborrino.SwiftHealthPocket.dto.request.CreaModificaStrutturaRequest;
import com.luanatamborrino.SwiftHealthPocket.dto.response.StrutturaResponse;
import com.luanatamborrino.SwiftHealthPocket.exception.BadRequestException;
import com.luanatamborrino.SwiftHealthPocket.exception.ConflictException;
import com.luanatamborrino.SwiftHealthPocket.exception.InternalServerErrorException;
import com.luanatamborrino.SwiftHealthPocket.exception.NotFoundException;
import com.luanatamborrino.SwiftHealthPocket.model.Struttura;
import com.luanatamborrino.SwiftHealthPocket.model.Utente;
import com.luanatamborrino.SwiftHealthPocket.model._enum.Ruolo;
import com.luanatamborrino.SwiftHealthPocket.repository.StrutturaRepository;
import com.luanatamborrino.SwiftHealthPocket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class StrutturaService {

    private final StrutturaRepository strutturaRepository;

    private final UserRepository userRepository;

    public void creaStruttura(CreaModificaStrutturaRequest request){

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
                .infermieri(new ArrayList<>())
                .build();

        //Salvo la struttura creata nel database.
        strutturaRepository.save(struttura);
    }

    public StrutturaResponse getStrutturaData(Long strutturaId){

        //Controllo che l'id parta da 1.
        if(strutturaId < 1) {
            throw new BadRequestException("Id non corretto.");
        }

        //Controllo se è presente una struttura con quell'id.
        Optional<Struttura> struttura = strutturaRepository.findById(strutturaId);

        //Se non viene trovata alcua struttura con l'ID fornito, viene lanciata l'eccezione.
        if(struttura.isEmpty()){
            throw new NotFoundException("Struttura non trovata.");
        }

        //Se esiste, viene creato e resituito il nuovo oggetto Struttura.
        return new StrutturaResponse(
                struttura.get().getId(),
                struttura.get().getNome(),
                struttura.get().getIndirizzo(),
                struttura.get().getCap()
        );
    }

    public List<StrutturaResponse> getAllStrutture(){

        //Prendo tutte le strutture dal database.
        List<Struttura> strutture = strutturaRepository.findAll();

        //Se non viene trovata alcua struttura con l'ID fornito, viene lanciata l'eccezione.
        if(strutture.isEmpty()){
            throw new NotFoundException("Strutture non trovate.");
        }

        //Creo la lists di strutture.
        List<StrutturaResponse> response = new ArrayList<>();

        //Per ogni struttura trovata, creo un oggetto StrutturaResponse e lo aggiungo alla lista.
        for (Struttura struttura : strutture ){
            response.add(
                new StrutturaResponse(
                    struttura.getId(),
                    struttura.getNome(),
                    struttura.getIndirizzo(),
                    struttura.getCap()
                )
            );

        }

        //Restituisco il DTO.
        return response;
    }

    public StrutturaResponse updateStruttura(Long strutturaId, CreaModificaStrutturaRequest request ){

        //Controllo che l'id parta da 1.
        if(strutturaId < 1) {
            throw new BadRequestException("Id non corretto.");
        }

        //Controllo se è presente una struttura con quell'id.
        Optional<Struttura> strutturaExists = strutturaRepository.findById(strutturaId);

        //Se non viene trovata alcua struttura con l'ID fornito, viene lanciata l'eccezione.
        if (strutturaExists.isEmpty()){
            throw new NotFoundException("Struttura non trovata.");
        }

        //Se esiste la struttura, la assegno ad una variabile.
        Struttura struttura = strutturaExists.get();

        //Controllo se il campo del nome non è vuoto e non contiene solo spazi bianchi, aggiorno il nome della struttura.
        if(!request.getNome().isBlank() && !request.getNome().isEmpty()){
            struttura.setNome(request.getNome());
        }

        //Controllo se il campo dll'indirizzo non è vuoto e non contiene solo spazi bianchi, aggiorno l'indirizzo della struttura.
        if(!request.getIndirizzo().isBlank() && !request.getIndirizzo().isEmpty()){
            struttura.setIndirizzo(request.getIndirizzo());
        }

        //Controllo se il campo del cap non è nullo e se la lunghezza della stringa è uguale a 5, aggiorno il cap della struttura.
        if(request.getCap() != null && String.valueOf(request.getCap()).length() == 5 ){
            struttura.setCap(request.getCap());
        }

        //Salvo la struttura nel database.
        strutturaRepository.save(struttura);


        //Ritotno l'oggetto struttura.
        return new StrutturaResponse(
                struttura.getId(),
                struttura.getNome(),
                struttura.getIndirizzo(),
                struttura.getCap());
    }

    public void deleteStrutturaById(Long id){

        //controllo che l'id parta da 1.
        if(id < 1) {
            throw new BadRequestException("Id non corretto.");
        }

        Optional<Struttura> struttura = strutturaRepository.findById(id);

        if (struttura.isEmpty()){
            throw new NotFoundException("Struttura non trovata.");
        }

        strutturaRepository.deleteById(struttura.get().getId());

        Optional<Struttura> strutturaDeleted = strutturaRepository.findById(struttura.get().getId());

        if(strutturaDeleted.isPresent()){
            throw new InternalServerErrorException("Errore nell'eliminazione.");
        }

    }

    public void associaInfermiere(AssociaDissociaInfermiereRequest request){

        //controllo che l'id parta da 1.
        if(request.getIdInfermiere() < 1 || request.getIdStruttura() < 1) {
            throw new BadRequestException("Id non corretto.");
        }

        Optional<Utente> optionalUser = userRepository.findById(request.getIdInfermiere());

        if (optionalUser.isEmpty()){
            throw new NotFoundException("Utente non trovato.");
        }

        Optional<Struttura> optionalStruttura = strutturaRepository.findById(request.getIdStruttura());

        if (optionalStruttura.isEmpty()){
            throw new NotFoundException("Struttura non trovata.");
        }

        if(!optionalUser.get().getRuolo().equals(Ruolo.INFERMIERE)){
            throw new BadRequestException("L'utente non è un infermiere.");
        }



        Utente user = optionalUser.get();
        Struttura struttura = optionalStruttura.get();

        List<Struttura> strutture = strutturaRepository.findAll();

        for(Struttura s : strutture){
            if(s.getInfermieri().contains(user)){
                throw new ConflictException("Infermiere già associato ad una struttura.");
            }
        }

        //TODO fix
        if(struttura.getInfermieri().isEmpty()){
            struttura.setInfermieri(new ArrayList<>(List.of(user)));
        }else {
            struttura.getInfermieri().add(user);
        }

        strutturaRepository.save(struttura);



    }

    public void dissociaInfermiere(AssociaDissociaInfermiereRequest request ){

        //controllo che l'id parta da 1.
        if(request.getIdInfermiere() < 1 || request.getIdStruttura() < 1) {
            throw new BadRequestException("Id non corretto.");
        }

        Optional<Utente> optionalUser = userRepository.findById(request.getIdInfermiere());

        if (optionalUser.isEmpty()){
            throw new NotFoundException("Utente non trovato.");
        }

        Optional<Struttura> optionalStruttura = strutturaRepository.findById(request.getIdStruttura());

        if (optionalStruttura.isEmpty()){
            throw new NotFoundException("Struttura non trovata.");
        }

        if(!optionalUser.get().getRuolo().equals(Ruolo.INFERMIERE)){
            throw new BadRequestException("L'utente non è un infermiere.");
        }



        Utente user = optionalUser.get();
        Struttura struttura = optionalStruttura.get();

        struttura.getInfermieri().remove(user);

        strutturaRepository.save(struttura);

    }
}
