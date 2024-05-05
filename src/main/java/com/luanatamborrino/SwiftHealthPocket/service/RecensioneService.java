package com.luanatamborrino.SwiftHealthPocket.service;

import com.luanatamborrino.SwiftHealthPocket.dto.request.RecensioneRequest;
import com.luanatamborrino.SwiftHealthPocket.dto.response.RecensioneResponse;
import com.luanatamborrino.SwiftHealthPocket.exception.BadRequestException;
import com.luanatamborrino.SwiftHealthPocket.exception.NotFoundException;
import com.luanatamborrino.SwiftHealthPocket.model.Prestazione;
import com.luanatamborrino.SwiftHealthPocket.model.Recensione;
import com.luanatamborrino.SwiftHealthPocket.model.Utente;
import com.luanatamborrino.SwiftHealthPocket.model._enum.Ruolo;
import com.luanatamborrino.SwiftHealthPocket.repository.PrestazioneRepository;
import com.luanatamborrino.SwiftHealthPocket.repository.RecensioneRepository;
import com.luanatamborrino.SwiftHealthPocket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecensioneService {

    private final RecensioneRepository recensioneRepository;

    private final PrestazioneRepository prestazioneRepository;

    private final UserRepository userRepository;

    public void salva(Long idPrestazione, RecensioneRequest request){

        //controllo che l'id parta da 1.
        if(idPrestazione < 1) {
            throw new BadRequestException("Id non corretto.");
        }

        //Prendo l'utente dal database con l'id fornito.
        Optional<Prestazione> prestazione = prestazioneRepository.findById(idPrestazione);

        //Se non viene trovato alcun utente con l'id fornito, lancio l'eccezione.
        if(prestazione.isEmpty()){
            throw new NotFoundException("Prestazione non trovata.");
        }


        //controllo che l'id parta da 1.
        if(request.getIdPaziente() < 1) {
            throw new BadRequestException("Id non corretto.");
        }

        //Prendo l'utente dal database con l'id fornito.
        Optional<Utente> paziente = userRepository.findById(request.getIdPaziente());

        //Se non viene trovato alcun utente con l'id fornito, lancio l'eccezione.
        if(paziente.isEmpty()){
            throw new NotFoundException("Paziente non trovato.");
        }

        //Controllo se l'utente trovato non ha il ruolo di infermiere, lancio l'eccezione.
        if(!paziente.get().getRuolo().equals(Ruolo.PAZIENTE)){
            throw new BadRequestException("Ruolo non corretto.");
        }

        if(request.getCommento().isEmpty() || request.getCommento().isBlank() || request.getCommento().length()>1000){
            throw new BadRequestException("Commento non valido.");
        }

        if(request.getValutazione() < 1 || request.getValutazione() >5){
            throw new BadRequestException("Valutazione non valida.");
        }

        if(prestazione.get().getEsito() == null){
            throw new BadRequestException("Esito non presente.");
        }

        Recensione recensione = Recensione.builder()
                .valutazione(request.getValutazione())
                .commento(request.getCommento())
                .paziente(paziente.get())
                .data(LocalDateTime.now())
                .prestazione(prestazione.get())
                .build();

        recensioneRepository.save(recensione);
    }

    public List<RecensioneResponse> getAllByPaziente (Long idPaziente){

        //controllo che l'id parta da 1.
        if(idPaziente < 1) {
            throw new BadRequestException("Id non corretto.");
        }

        //Prendo l'utente dal database con l'id fornito.
        Optional<Utente> paziente = userRepository.findById(idPaziente);

        //Se non viene trovato alcun utente con l'id fornito, lancio l'eccezione.
        if(paziente.isEmpty()){
            throw new NotFoundException("Paziente non trovato.");
        }

        //Controllo se l'utente trovato non ha il ruolo di infermiere, lancio l'eccezione.
        if(!paziente.get().getRuolo().equals(Ruolo.PAZIENTE)){
            throw new BadRequestException("Ruolo non corretto.");
        }

        List<Recensione> recensioni = recensioneRepository.findAllByPaziente(paziente.get());

        List<RecensioneResponse> response = new ArrayList<>();

        for(Recensione recensione: recensioni){

            response.add(new RecensioneResponse(
                    recensione.getCommento(),
                    recensione.getValutazione(),
                    recensione.getPrestazione().getIdPrestazione(),
                    recensione.getPaziente().getNome(),
                    recensione.getData()
            ));
        }

        return response;
    }

    public RecensioneResponse getByPrestazioneId (Long idPrestazione){

        //controllo che l'id parta da 1.
        if(idPrestazione < 1) {
            throw new BadRequestException("Id non corretto.");
        }

        //Prendo l'utente dal database con l'id fornito.
        Optional<Prestazione> prestazione = prestazioneRepository.findById(idPrestazione);

        //Se non viene trovato alcun utente con l'id fornito, lancio l'eccezione.
        if(prestazione.isEmpty()){
            throw new NotFoundException("Prestazione non trovata.");
        }


        Recensione recensione = recensioneRepository.findByPrestazione(prestazione.get());


        return new RecensioneResponse(
                recensione.getCommento(),
                recensione.getValutazione(),
                recensione.getPrestazione().getIdPrestazione(),
                recensione.getPaziente().getNome(),
                recensione.getData()
        );
    }
}

