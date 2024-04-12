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
import com.luanatamborrino.SwiftHealthPocket.observer.publisher.Publisher;
import com.luanatamborrino.SwiftHealthPocket.repository.StrutturaRepository;
import com.luanatamborrino.SwiftHealthPocket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service per gestire tutti i metodi riguardanti la struttura.
 */
@Service
@RequiredArgsConstructor
public class StrutturaService {

    private final StrutturaRepository strutturaRepository;
    private final UserRepository userRepository;

    private final Publisher publisher;

    /**
     * Metodo per creare una struttura nel database.
     * @param request DTO con i dati della creazione della struttura.
     */
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

    /**
     * Metodo che, dato un id, prende la struttura dal database tramite la repository e la ritorna al controller.
     * @param idStruttura Id della struttura.
     * @return DTO con i dati della struttura.
     */
    public StrutturaResponse getStrutturaData(Long idStruttura){

        //Controllo che l'id parta da 1.
        if(idStruttura < 1) {
            throw new BadRequestException("Id non corretto.");
        }

        //Controllo se è presente una struttura con quell'id.
        Optional<Struttura> struttura = strutturaRepository.findById(idStruttura);

        //Se non viene trovata alcua struttura con l'ID fornito, viene lanciata l'eccezione.
        if(struttura.isEmpty()){
            throw new NotFoundException("Struttura non trovata.");
        }

        //Ritorno il DTO con i dati dell'utente richiesto.
        return new StrutturaResponse(
                struttura.get().getId(),
                struttura.get().getNome(),
                struttura.get().getIndirizzo(),
                struttura.get().getCap()
        );
    }

    /**
     * Metodo per prenere tutte le strutture presenti sul databse.
     * @return Lista di DTO con i dati di ogni struttura.
     */
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
            response.add(new StrutturaResponse(
                    struttura.getId(),
                    struttura.getNome(),
                    struttura.getIndirizzo(),
                    struttura.getCap()
                ));
        }

        //Restituisco il DTO.
        return response;
    }

    /**
     * Metodo utilizzato per modificare i dati di una struttura.
     * @param idStruttura Id della struttura da modificare.
     * @param request DTO con i nuovi dati.
     * @return DTO con i dati della struttura modificata.
     */
    public StrutturaResponse updateStruttura(Long idStruttura, CreaModificaStrutturaRequest request ){

        //Controllo che l'id parta da 1.
        if(idStruttura < 1) {
            throw new BadRequestException("Id non corretto.");
        }

        //Controllo se è presente una struttura con quell'id.
        Optional<Struttura> strutturaExists = strutturaRepository.findById(idStruttura);

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


        //Ritorno il DTO che conterrà i dettagli aggiornati della struttura.
        return new StrutturaResponse(
                struttura.getId(),
                struttura.getNome(),
                struttura.getIndirizzo(),
                struttura.getCap()
        );
    }

    /**
     * Metodo che, dato l'id, viene eliminata la struttura dal database.
     * @param idStruttura Id della struttura da eliminare
     */
    public void deleteStrutturaById(Long idStruttura){

        //controllo che l'id parta da 1.
        if(idStruttura < 1) {
            throw new BadRequestException("Id non corretto.");
        }

        //Controllo se è presente una struttura con quell'id.
        Optional<Struttura> struttura = strutturaRepository.findById(idStruttura);

        //Se non viene trovata alcuna struttura con l'id fornito, lancio l'eccezione.
        if (struttura.isEmpty()){
            throw new NotFoundException("Struttura non trovata.");
        }

        //Elimino la struttura trovata dal database.
        strutturaRepository.deleteById(struttura.get().getId());

        //Verifico se la struttura è stata effettivamente eliminata.
        Optional<Struttura> strutturaDeleted = strutturaRepository.findById(struttura.get().getId());

        //Se la struttura è ancora presente dopo la cancellazione, lancio l'eccezione.
        if(strutturaDeleted.isPresent()){
            throw new InternalServerErrorException("Errore nell'eliminazione.");
        }
    }

    /**
     *
     * @param request
     */
    public void associaInfermiere(AssociaDissociaInfermiereRequest request){

        //controllo che l'id parta da 1.
        if(request.getIdInfermiere() < 1 || request.getIdStruttura() < 1) {
            throw new BadRequestException("Id non corretto.");
        }

        //Controllo se è presente un utente con quell'id.
        Optional<Utente> optionalUser = userRepository.findById(request.getIdInfermiere());

        //Se non viene trovato alcun utente con l'id fornito, lancio l'eccezione.
        if (optionalUser.isEmpty()){
            throw new NotFoundException("Utente non trovato.");
        }

        //Controllo se è presente una struttura con quell'id.
        Optional<Struttura> optionalStruttura = strutturaRepository.findById(request.getIdStruttura());

        //Se non viene trovata alcuna struttura con l'id fornito, lancio l'eccezione.
        if (optionalStruttura.isEmpty()){
            throw new NotFoundException("Struttura non trovata.");
        }

        //Se l'utente non ha il ruolo di infermiere, lancio l'eccezione.
        if(!optionalUser.get().getRuolo().equals(Ruolo.INFERMIERE)){
            throw new BadRequestException("L'utente non è un infermiere.");
        }

        //Se esiste l'utente, lo assegno ad una variabile.
        Utente user = optionalUser.get();

        //Se esiste la struttura, la assegno ad una variabile.
        Struttura struttura = optionalStruttura.get();

        //Controllo se l'utente è già associato ad una struttura lancio l'eccezione.
        if(user.getStruttura() != null){
            throw new ConflictException("Utente già associato.");
        }

        user.setStruttura(struttura);

        userRepository.save(user);

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

        user.setStruttura(null);

        userRepository.save(user);


        Optional<Utente> optionalAdmin = userRepository.findByRuolo(Ruolo.AMMINISTRATORE);

        if (optionalAdmin.isEmpty()){
            throw new NotFoundException("Amministratore non trovato.");
        }
        publisher.notify("InfermiereDissociato",
                user.getNome(),
                user.getCognome(),
                "",
                "",
                optionalAdmin.get().getEmail());

    }
}
