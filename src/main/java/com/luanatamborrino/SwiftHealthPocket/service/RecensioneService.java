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
import com.luanatamborrino.SwiftHealthPocket.util.Methods;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service per gestire tutti i metodi riguardanti la struttura.
 */
@Service
@RequiredArgsConstructor
public class RecensioneService {

    private final RecensioneRepository recensioneRepository;
    private final PrestazioneRepository prestazioneRepository;
    private final UserRepository userRepository;

    /**
     * Metodo per salvare una nuova recensione per una prestazione.
     * @param idPrestazione Id della prestazione da recensire.
     * @param request DTO contenente i dati della recensione.
     */
    public void salva(Long idPrestazione, RecensioneRequest request) {

        //Verifico che l'id di paziente e di prestazione siano validi e non nulli.
        Methods.getInstance().checkIds(List.of(
                idPrestazione,
                request.getIdPaziente()
        ));

        //Prendo dal database la prestazione con l'id fornito.
        Optional<Prestazione> prestazione = prestazioneRepository.findById(idPrestazione);

        //Se non viene trovata alcuna prestazione con l'id fornito, lancio l'eccezione.
        if(prestazione.isEmpty()) {
            throw new NotFoundException("Prestazione non trovata.");
        }

        //Prendo dal database il paziente con l'id fornito.
        Optional<Utente> paziente = userRepository.findById(request.getIdPaziente());

        //Se non viene trovato alcun paziente con l'id fornito, lancio l'eccezione.
        if(paziente.isEmpty()) {
            throw new NotFoundException("Paziente non trovato.");
        }

        //Se l'utente non ha il ruolo di paziente, lancio l'eccezione.
        if(!paziente.get().getRuolo().equals(Ruolo.PAZIENTE)) {
            throw new BadRequestException("Ruolo non corretto.");
        }

        //Invoco il metodo checkStringData per verificare che il campo 'commento' non sia vuoto o nullo.
        Methods.getInstance().checkStringData(List.of(
                request.getCommento()
        ));

        //Controllo se la lunghezza del campo 'commento' supera il limite massimo di 1000 caratteri.
        if(request.getCommento().length() > 1000) {
            throw new BadRequestException("Commento non valido.");
        }

        //Verifico che il campo 'valutazione' sia un dato intero valido.
        Methods.getInstance().checkIntegerData(List.of(
                request.getValutazione()
        ));

        //Verifico che la valutazione sia compresa tra 1 e 5.
        if(request.getValutazione() < 1 || request.getValutazione() > 5) {
            throw new BadRequestException("Valutazione non valida.");
        }

        //Verifico che la prestazione abbia un esito registrato.
        if(prestazione.get().getEsito() == null) {
            throw new BadRequestException("Esito non presente.");
        }

        //Creo l'oggetto Recensione con il pattern builder e lo salvo nel database.
        Recensione recensione = prestazione.get().getRecensione();
        recensione.setCommento(request.getCommento());
        recensione.setValutazione(request.getValutazione());
        recensione.setPaziente(paziente.get());
        recensione.setData(LocalDateTime.now());

        //Salvo la recensione nel database.
        recensioneRepository.save(recensione);
    }

    /**
     * Metodo che recupera tutte le recensioni fatte da un paziente specifico.
     * @param idPaziente Id del paziente di cui recupereare le recensioni.
     * @return Lista di DTO che rappresenta le recensioni fatte dal paziente.
     */
    public List<RecensioneResponse> getAllByPaziente (Long idPaziente) {

        //Verifico che l'id di paziente sia valido e non nullo.
        Methods.getInstance().checkIds(List.of(
                idPaziente
        ));

        //Prendo dal database il paziente con l'id fornito.
        Optional<Utente> paziente = userRepository.findById(idPaziente);

        //Se non viene trovato alcun paziente con l'id fornito, lancio l'eccezione.
        if(paziente.isEmpty()) {
            throw new NotFoundException("Paziente non trovato.");
        }

        //Se l'utente non ha il ruolo di paziente, lancio l'eccezione.
        if(!paziente.get().getRuolo().equals(Ruolo.PAZIENTE)) {
            throw new BadRequestException("Ruolo non corretto.");
        }

        //Recupero tutte le recensioni associate al paziente.
        List<Recensione> recensioni = recensioneRepository.findAllByPaziente(paziente.get());

        //Creo la lista di recensioni.
        List<RecensioneResponse> response = new ArrayList<>();

        //Per ogni recensione, creo un nuovo oggetto RecensioneResponse con i dati della recensione.
        for(Recensione recensione: recensioni) {
            response.add(new RecensioneResponse(
                    recensione.getCommento(),
                    recensione.getValutazione(),
                    recensione.getPrestazione().getIdPrestazione(),
                    recensione.getPaziente().getNome(),
                    recensione.getData()
            ));
        }

        //Restituisco il DTO.
        return response;
    }

    /**
     * Metodo per recuperare la recensione associata a una specifica prestazione.
     * @param idPrestazione Id della prestazione di cui recuperare la recensione.
     * @return DTO con i dati della recensione.
     */
    public RecensioneResponse getByPrestazioneId (Long idPrestazione) {

        //Verifico che l'id di prestazione sia valido e non nullo.
        Methods.getInstance().checkIds(List.of(
                idPrestazione
        ));

        //Prendo dal database la prestazione con l'id fornito.
        Optional<Prestazione> prestazione = prestazioneRepository.findById(idPrestazione);

        //Se non viene trovata alcuna prestazione con l'id fornito, lancio l'eccezione.
        if(prestazione.isEmpty()) {
            throw new NotFoundException("Prestazione non trovata.");
        }

        //Prendo dal database la recensione associata alla prestazione.
        Recensione recensione = recensioneRepository.findByPrestazione(prestazione.get());

        //Ritorno il DTO che conterrà i dati della recensione.
        return new RecensioneResponse(
                recensione.getCommento(),
                recensione.getValutazione(),
                recensione.getPrestazione().getIdPrestazione(),
                recensione.getPaziente().getNome(),
                recensione.getData()
        );
    }
}

